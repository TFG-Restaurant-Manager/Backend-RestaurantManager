package com.tfg_rm.backend_restaurantmanager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg_rm.backend_restaurantmanager.dto.TableRequest;
import com.tfg_rm.backend_restaurantmanager.dto.TableResponse;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.TableMapper;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.entity.TableSectionsEntity;
import com.tfg_rm.backend_restaurantmanager.entity.TableStatusEntity;
import com.tfg_rm.backend_restaurantmanager.entity.TablesRestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.repository.RestaurantRepository;
import com.tfg_rm.backend_restaurantmanager.repository.TableSectionsRepository;
import com.tfg_rm.backend_restaurantmanager.repository.TablesRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TableService {

    private final TablesRepository tablesRepository;
    private final RestaurantRepository restaurantRepository;
    private final TableSectionsRepository tableSectionsRepository;
    private final EntityManager entityManager;

    public List<TableResponse> getTableInfo(Long restaurantId) {
        List<TableResponse> tables = tablesRepository
                .findByRestaurantId(restaurantId)
                .stream()
                .map(TableMapper::toResponse)
                .collect(Collectors.toList());

        return tables;
    }

    @Transactional
    public List<TableResponse> updateAllTables(Long restaurantId, List<TableRequest> tableRequests) {
        // Fetch restaurant
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Fetch current tables
        List<TablesRestaurantEntity> currentTables = tablesRepository.findByRestaurantId(restaurantId);

        // IDs from request
        Set<Long> requestIds = tableRequests.stream()
                .map(TableRequest::getTableId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // IDs of tables to delete: current ones not in request
        List<Long> tableIdsToDelete = currentTables.stream()
                .filter(t -> !requestIds.contains(t.getId()))
                .map(TablesRestaurantEntity::getId)
                .collect(Collectors.toList());

        // Use JPQL bulk deletes to avoid Hibernate persistence context issues.
        // First delete order_table records that reference the tables being deleted,
        // then delete the tables themselves.
        if (!tableIdsToDelete.isEmpty()) {
            entityManager.createQuery(
                    "DELETE FROM OrderTableEntity ot WHERE ot.table.id IN :tableIds")
                    .setParameter("tableIds", tableIdsToDelete)
                    .executeUpdate();

            entityManager.createQuery(
                    "DELETE FROM TablesRestaurantEntity t WHERE t.id IN :tableIds")
                    .setParameter("tableIds", tableIdsToDelete)
                    .executeUpdate();

            // Flush pending SQL and clear the persistence context to avoid stale references
            entityManager.flush();
            entityManager.clear();
        }

        // Re-fetch restaurant after clearing persistence context
        restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Update or Add
        List<TablesRestaurantEntity> tablesToSave = new ArrayList<>();
        Map<String, TableSectionsEntity> createdSections = new HashMap<>();
        for (TableRequest req : tableRequests) {

            TablesRestaurantEntity entity;
            if (req.getTableId() != null) {
                entity = tablesRepository.findById(req.getTableId())
                        .orElse(new TablesRestaurantEntity());
            } else {
                entity = new TablesRestaurantEntity();
            }

            entity.setRestaurant(restaurant);
            entity.setName(req.getTableName());
            entity.setCapacity(req.getCapacity());
            entity.setPosX(req.getPosX());
            entity.setPosY(req.getPosY());

            // Set section
            TableSectionsEntity section;

            if (req.getSectionId() != null) {

                section = tableSectionsRepository.findById(req.getSectionId())
                        .orElseThrow(() -> new RuntimeException("Section not found: " + req.getSectionId()));

            } else {

                // Si no viene ID, debe venir nombre
                if (req.getSectionName() == null || req.getSectionName().isBlank()) {
                    throw new RuntimeException("Section name is required when sectionId is null");
                }

                String sectionName = req.getSectionName().trim();

                // Mirar si ya se creó en esta misma request
                if (createdSections.containsKey(sectionName)) {

                    section = createdSections.get(sectionName);

                } else {

                    // Crear nueva sección
                    section = new TableSectionsEntity();
                    section.setTitle(sectionName);
                    section.setRestaurant(restaurant);

                    section = tableSectionsRepository.save(section);

                    // Guardarla en caché
                    createdSections.put(sectionName, section);
                }
            }

            entity.setSection(section);

            // If new, set default status if needed
            if (entity.getStatus() == null) {
                entity.setStatus(TableStatusEntity.AVAILABLE);
            }

            tablesToSave.add(entity);

        }

        List<TablesRestaurantEntity> savedTables = tablesRepository.saveAll(tablesToSave);

        // Identificar secciones que tienen mesas después de la actualización
        Set<Long> sectionIdsWithTables = savedTables.stream()
                .map(TablesRestaurantEntity::getSection)
                .filter(Objects::nonNull)
                .map(TableSectionsEntity::getId)
                .collect(Collectors.toSet());

        // Buscar todas las secciones del restaurante y eliminar las que no tengan mesas
        List<TableSectionsEntity> allSections = tableSectionsRepository.findByRestaurantId(restaurantId);

        List<TableSectionsEntity> sectionsToDelete = allSections.stream()
                .filter(s -> !sectionIdsWithTables.contains(s.getId()))
                .collect(Collectors.toList());

        if (!sectionsToDelete.isEmpty()) {
            tableSectionsRepository.deleteAll(sectionsToDelete);
        }

        return savedTables.stream()
                .map(TableMapper::toResponse)
                .collect(Collectors.toList());
    }
}

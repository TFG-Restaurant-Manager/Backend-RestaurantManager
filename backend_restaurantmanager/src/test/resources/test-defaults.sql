-- Run after Hibernate creates the schema (via @Sql on @DataJpaTest classes).
-- Adds DEFAULT NOW() to columns that are insertable=false in entities (set by DB trigger in production).
ALTER TABLE restaurants ALTER COLUMN created_at SET DEFAULT NOW();
ALTER TABLE orders ALTER COLUMN created_at SET DEFAULT NOW();

-- ====================================================================
-- Assignment & Submission Management System
-- Reset Database Script (Drops, Re-creates, and Re-seeds Database)
-- ====================================================================

DROP DATABASE IF EXISTS assignment_manager;
CREATE DATABASE assignment_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE assignment_manager;

-- Run Schema
SOURCE schema.sql;

-- Run Seed Data
SOURCE seed.sql;

SELECT 'Database reset and seed complete!' AS Status;

ALTER TABLE API_PROJECT RENAME COLUMN DESCRIPTION TO DESCRIPTION_EN;
ALTER TABLE API_PROJECT ADD COLUMN DESCRIPTION_FR VARCHAR(510) NOT NULL DEFAULT '';

ALTER TABLE API_IMAGE RENAME COLUMN ALT TO ALT_EN;
ALTER TABLE API_IMAGE ADD COLUMN ALT_FR VARCHAR(255) NOT NULL DEFAULT '';

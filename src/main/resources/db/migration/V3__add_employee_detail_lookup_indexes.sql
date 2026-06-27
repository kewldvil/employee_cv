CREATE INDEX IF NOT EXISTS idx_file_upload_user_id
    ON file_upload (user_id);

CREATE INDEX IF NOT EXISTS idx_police_plate_number_car_employee_id
    ON police_plate_number_car (employee_id);

CREATE INDEX IF NOT EXISTS idx_weapon_employee_id
    ON weapon (employee_id);

CREATE INDEX IF NOT EXISTS idx_appreciation_employee_id
    ON appreciation (employee_id);

CREATE INDEX IF NOT EXISTS idx_vocational_training_employee_id
    ON vocational_training (employee_id);

CREATE INDEX IF NOT EXISTS idx_previous_activity_and_position_employee_id
    ON previous_activity_and_position (employee_id);

CREATE INDEX IF NOT EXISTS idx_employee_degree_level_employee_id
    ON employee_degree_level (employee_id);

CREATE INDEX IF NOT EXISTS idx_employee_language_employee_id
    ON employee_language (employee_id);

CREATE INDEX IF NOT EXISTS idx_spouse_children_spouse_id
    ON spouse_children (spouse_id);

CREATE INDEX IF NOT EXISTS idx_address_provinces_address_id
    ON address_provinces (address_id);

CREATE INDEX IF NOT EXISTS idx_address_districts_address_id
    ON address_districts (address_id);

CREATE INDEX IF NOT EXISTS idx_address_communes_address_id
    ON address_communes (address_id);

CREATE INDEX IF NOT EXISTS idx_address_villages_address_id
    ON address_villages (address_id);

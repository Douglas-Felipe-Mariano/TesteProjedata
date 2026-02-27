CREATE TABLE raw_material (
    mat_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    mat_name VARCHAR(100) NOT NULL,
    mat_quantity DECIMAL(10,2) NOT NULL,
    unit_id INTEGER NOT NULL,

    CONSTRAINT fk_unit FOREIGN KEY (unit_id)
                       REFERENCES unit_of_measure(unit_id)
);

CREATE TABLE product (
    prod_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    prod_name VARCHAR(255) NOT NULL,
    prod_price DECIMAL(10,2) NOT NULL,
    prod_description VARCHAR(255)
);

CREATE TABLE product_composition (
    prod_id INTEGER NOT NULL,
    mat_id INTEGER NOT NULL,
    quantity_needed DECIMAL(10,2) NOT NULL,
    
    PRIMARY KEY (prod_id, mat_id),

    CONSTRAINT fk_product FOREIGN KEY (prod_id)
                          REFERENCES product(prod_id)
                          ON DELETE CASCADE,

    CONSTRAINT fk_raw_material FOREIGN KEY (mat_id)
                               REFERENCES raw_material(mat_id)
);

CREATE TABLE unit_of_measure (
    unit_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    unit_name VARCHAR(50) NOT NULL,
    unit_symbol VARCHAR(10) NOT NULL
);
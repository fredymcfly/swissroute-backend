-- Migrations V2: Agregar constraint UNIQUE para nombre de ruta por usuario
-- Esto evita que un usuario tenga múltiples rutas con el mismo nombre

-- Crear índice único en la combinación de usuario_id y nombre
ALTER TABLE rutas_favoritas 
ADD CONSTRAINT unique_ruta_por_usuario 
UNIQUE (usuario_id, nombre);

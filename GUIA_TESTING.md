# 🧪 GUÍA DE TESTING - CRUD de Productos

## 📋 Requisitos Previos

- ✅ Aplicación compilada
- ✅ Emulador Android o dispositivo físico
- ✅ API backend corriendo y accesible
- ✅ Usuario autenticado en la app
- ✅ Conexión a internet funcional

---

## 🎯 Casos de Prueba

### Test 1: Crear Producto (CREAR - CREATE)

#### Caso 1.1: Crear producto válido

**Precondiciones:**
- Usuario está en "Gestión de Inventario"
- Hay conexión a la API

**Pasos:**
1. Presiona el botón FAB (+) en la esquina inferior derecha
2. Se abre `AddProductBottomSheet`
3. Completa los campos:
   ```
   Código: HCOR-001
   Nombre: Broca HSS 5mm
   Categoría: Herramientas
   Descripción: Broca de acero templado para metal
   Precio: 1500
   Stock: 50
   ```
4. Presiona botón "Agregar"

**Resultado Esperado:**
- ✅ Snackbar verde: "Producto creado exitosamente"
- ✅ El formulario se cierra automáticamente
- ✅ El nuevo producto aparece en la lista
- ✅ El producto tiene código HCOR-001
- ✅ El stock muestra 50

**Validación Backend:**
- [ ] Verificar en BD que el producto fue creado con ID
- [ ] Verificar que tiene los datos correctos
- [ ] Verificar que está activo

---

#### Caso 1.2: Código con formato incorrecto

**Pasos:**
1. Abre formulario de crear
2. Ingresa código: `HCOR-1` (formato inválido)
3. Intenta presionar "Agregar"

**Resultado Esperado:**
- ✅ Campo "Código" se marca rojo
- ✅ Mensaje de error: "El formato debe ser AAAA-123"
- ✅ Botón "Agregar" no funciona (deshabilitado)
- ✅ No se envía nada a API

---

#### Caso 1.3: Código duplicado

**Pasos:**
1. Abre formulario de crear
2. Ingresa código: `HCOR-001` (que ya existe)
3. Intenta presionar "Agregar"

**Resultado Esperado:**
- ✅ Campo "Código" se marca rojo
- ✅ Mensaje de error: "El código ya existe"
- ✅ Botón "Agregar" deshabilitado
- ✅ No se envía a API

---

#### Caso 1.4: Campos obligatorios vacíos

**Pasos:**
1. Abre formulario de crear
2. Deja algunos campos vacíos (nombre, categoría, precio)
3. Intenta presionar "Agregar"

**Resultado Esperado:**
- ✅ Se marcan en rojo los campos vacíos
- ✅ Mensajes de error para cada uno:
   - "El código es obligatorio"
   - "El nombre es obligatorio"
   - "La categoría es obligatoria"
   - "El precio es obligatorio"
   - "El stock es obligatorio"
- ✅ Botón "Agregar" deshabilitado

---

#### Caso 1.5: Precio negativo

**Pasos:**
1. Abre formulario de crear
2. Ingresa precio: `-500`
3. Intenta presionar "Agregar"

**Resultado Esperado:**
- ✅ Campo "Precio" se marca rojo
- ✅ Mensaje: "El precio no puede ser negativo"
- ✅ Botón deshabilitado

---

#### Caso 1.6: Stock con letras

**Pasos:**
1. Abre formulario de crear
2. Ingresa stock: `abc`
3. Intenta presionar "Agregar"

**Resultado Esperado:**
- ✅ El campo solo permite números (no acepta letras)
- ✅ Stock queda vacío o con valor anterior

---

### Test 2: Editar Producto (UPDATE - ACTUALIZAR)

#### Caso 2.1: Editar producto - cambiar stock

**Precondiciones:**
- Existe producto HCOR-001 con stock 50

**Pasos:**
1. En la lista de inventario, localiza HCOR-001
2. Presiona el ícono EDIT (lápiz azul)
3. Se abre `EditProductScreen`
4. Verifica que los datos están precargados:
   ```
   Código: HCOR-001 ✓
   Nombre: Broca HSS 5mm ✓
   Categoría: Herramientas ✓
   Precio: 1500 ✓
   Stock: 50 ✓
   ```
5. Cambia el stock a `100`
6. Presiona "Guardar Cambios"

**Resultado Esperado:**
- ✅ Snackbar verde: "Producto actualizado exitosamente"
- ✅ Navega automáticamente atrás a la lista
- ✅ El producto ahora muestra Stock: 100
- ✅ Otros datos se mantienen igual

**Validación Backend:**
- [ ] Verificar en BD que el stock se actualizó a 100
- [ ] Verificar que otros campos no cambiaron
- [ ] Verificar que el ID se mantuvo igual

---

#### Caso 2.2: Editar producto - cambiar código (válido)

**Pasos:**
1. Abre un producto para editar
2. Cambia el código de `HCOR-001` a `HCOR-002`
3. Presiona "Guardar Cambios"

**Resultado Esperado:**
- ✅ Snackbar de éxito
- ✅ El producto ahora tiene código HCOR-002
- ✅ Se actualizó en API

---

#### Caso 2.3: Editar producto - código a duplicado

**Pasos:**
1. Abre HCOR-001 para editar
2. Intenta cambiar código a `HCOR-003` (que ya existe)
3. Intenta presionar "Guardar Cambios"

**Resultado Esperado:**
- ✅ Campo "Código" se marca rojo
- ✅ Mensaje: "El código ya existe"
- ✅ Botón deshabilitado
- ✅ No se envía a API

---

#### Caso 2.4: Editar - mantener código original

**Pasos:**
1. Abre HCOR-001
2. Deja todo igual (incluido el código)
3. Presiona "Guardar Cambios"

**Resultado Esperado:**
- ✅ Snackbar de éxito (aunque no haya cambios)
- ✅ Vuelve a la lista
- ✅ Datos sin cambios (pero API actualizó)

---

#### Caso 2.5: Editar - error de conexión

**Pasos:**
1. Desactiva Wi-Fi/datos
2. Abre producto para editar
3. Cambia algo
4. Presiona "Guardar Cambios"

**Resultado Esperado:**
- ✅ Snackbar rojo con error: "Error al actualizar: [motivo]"
- ✅ No navega atrás
- ✅ Usuario puede reintentar

---

### Test 3: Eliminar Producto (DELETE - ELIMINAR)

#### Caso 3.1: Eliminar producto - confirmación

**Precondiciones:**
- Existe producto HCOR-001 visible en lista

**Pasos:**
1. En la lista, localiza HCOR-001
2. Presiona el ícono DELETE (basura roja)
3. Se abre `DeleteConfirmationDialog`
4. Verifica el diálogo:
   ```
   [Icono advertencia] 🚨
   Confirmar eliminación
   ¿Estás seguro de que deseas eliminar el 
   producto "Broca HSS 5mm"? 
   Esta acción no se puede deshacer.
   
   [Cancelar]  [Eliminar]
   ```
5. Presiona "Eliminar"

**Resultado Esperado:**
- ✅ Snackbar verde: "Producto eliminado exitosamente"
- ✅ El diálogo se cierra
- ✅ El producto desaparece de la lista
- ✅ La lista se actualiza

**Validación Backend:**
- [ ] Verificar en BD que el producto está marcado como inactivo o eliminado
- [ ] Verificar que no aparece en GET /api/v1/productos

---

#### Caso 3.2: Eliminar producto - cancelar

**Pasos:**
1. Presiona DELETE en un producto
2. Se abre el diálogo
3. Presiona "Cancelar"

**Resultado Esperado:**
- ✅ Diálogo se cierra
- ✅ El producto sigue en la lista
- ✅ No se envía DELETE a API

---

#### Caso 3.3: Eliminar producto - error en API

**Pasos:**
1. Desactiva conexión
2. Presiona DELETE en un producto
3. Confirma eliminación

**Resultado Esperado:**
- ✅ Snackbar rojo: "Error al eliminar: [motivo]"
- ✅ Diálogo se cierra
- ✅ El producto sigue en la lista (no se filtró localmente)

---

### Test 4: Filtros y Búsqueda (Contexto)

#### Caso 4.1: Crear producto y que aparezca filtrado

**Pasos:**
1. Establece filtro: Categoría = "Herramientas"
2. Abre formulario de crear
3. Crea producto con Categoría: "Herramientas"
4. Completa y presiona "Agregar"

**Resultado Esperado:**
- ✅ El nuevo producto aparece en la lista (pasa el filtro)
- ✅ Se ve inmediatamente

---

#### Caso 4.2: Buscar producto creado

**Pasos:**
1. En la búsqueda, escribe: `Broca`
2. Verifica que aparezca el producto creado

**Resultado Esperado:**
- ✅ El producto HCOR-001 "Broca HSS 5mm" aparece
- ✅ Se busca por nombre
- ✅ También funciona si buscas por código: `HCOR-001`

---

### Test 5: Responsive Design

#### Caso 5.1: Crear en vista Compact

**Pasos:**
1. Emulador en portrait mode (411dp)
2. Abre "Gestión de Inventario"
3. Usa los casos de crear/editar/eliminar

**Resultado Esperado:**
- ✅ Todo funciona igual
- ✅ Los formularios se ven bien en pantalla pequeña

---

#### Caso 5.2: Crear en vista Medium

**Pasos:**
1. Emulador en landscape mode (600dp)
2. Repite los casos de crear/editar/eliminar

**Resultado Esperado:**
- ✅ Todo funciona igual
- ✅ Los formularios se adaptan mejor

---

#### Caso 5.3: Crear en vista Expanded

**Pasos:**
1. Tablet en landscape (1200dp+)
2. Repite los casos de crear/editar/eliminar

**Resultado Esperado:**
- ✅ Todo funciona igual
- ✅ El layout es el más espacioso

---

## 📊 Matriz de Pruebas

| # | Caso | Acción | Esperado | Estado |
|---|------|--------|----------|--------|
| 1.1 | Crear válido | ADD | ✅ Snackbar + aparece | [ ] |
| 1.2 | Código inválido | ADD | ❌ Error formato | [ ] |
| 1.3 | Código duplicado | ADD | ❌ Error duplicado | [ ] |
| 1.4 | Campos vacíos | ADD | ❌ Campos rojo | [ ] |
| 1.5 | Precio negativo | ADD | ❌ Error precio | [ ] |
| 1.6 | Stock letras | ADD | ❌ No acepta letras | [ ] |
| 2.1 | Editar stock | UPDATE | ✅ Stock actualizado | [ ] |
| 2.2 | Cambiar código | UPDATE | ✅ Código actualizado | [ ] |
| 2.3 | Código a duplicado | UPDATE | ❌ Error duplicado | [ ] |
| 2.4 | Mantener igual | UPDATE | ✅ Actualización OK | [ ] |
| 2.5 | Error conexión | UPDATE | ❌ Snackbar error | [ ] |
| 3.1 | Eliminar OK | DELETE | ✅ Desaparece | [ ] |
| 3.2 | Cancelar delete | DELETE | ❌ Mantiene producto | [ ] |
| 3.3 | Error conexión | DELETE | ❌ Snackbar error | [ ] |
| 4.1 | Crear filtrado | CREATE | ✅ Aparece filtrado | [ ] |
| 4.2 | Buscar creado | SEARCH | ✅ Aparece en búsqueda | [ ] |
| 5.1 | Compact view | ALL | ✅ Funciona todo | [ ] |
| 5.2 | Medium view | ALL | ✅ Funciona todo | [ ] |
| 5.3 | Expanded view | ALL | ✅ Funciona todo | [ ] |

---

## 🔍 Validación Backend (SQL)

Después de cada operación, verifica en la BD:

### Después de Crear HCOR-001:
```sql
SELECT * FROM productos WHERE codigo = 'HCOR-001';
-- Debe retornar 1 fila con:
-- id: (número asignado)
-- codigo: HCOR-001
-- nombre: Broca HSS 5mm
-- stock: 50
-- precio: 1500
-- activo: true
```

### Después de Actualizar (stock = 100):
```sql
SELECT stock FROM productos WHERE codigo = 'HCOR-001';
-- Debe retornar: 100
```

### Después de Eliminar:
```sql
SELECT * FROM productos WHERE codigo = 'HCOR-001';
-- Debe retornar vacío (o activo = false si es soft delete)
```

---

## 🐛 Bugs Comunes a Buscar

| Síntoma | Verificar |
|---------|-----------|
| No se crea producto | ¿API devolvió DTO con ID? |
| Producto no aparece después de crear | ¿Se actualizó _allInventoryItems? |
| Error al editar | ¿Se obtiene ID del caché correctamente? |
| Eliminación no funciona | ¿Se envía DELETE request? |
| Stock no se actualiza | ¿El backend recibió el valor? |
| Snackbar no muestra | ¿Se limpia operationState? |
| Código duplicado no se valida | ¿Se está buscando en inventoryList? |
| Cambio no persiste | ¿Se actualizó el caché _productoDTOs? |

---

## 🚀 Pasos para Testing Completo

### Día 1: Crear y Listar
- [ ] Caso 1.1: Crear válido
- [ ] Caso 1.2: Código inválido
- [ ] Caso 1.3: Código duplicado
- [ ] Caso 1.4: Campos vacíos

### Día 2: Validaciones
- [ ] Caso 1.5: Precio negativo
- [ ] Caso 1.6: Stock letras
- [ ] Caso 4.2: Búsqueda

### Día 3: Editar
- [ ] Caso 2.1: Cambiar stock
- [ ] Caso 2.2: Cambiar código
- [ ] Caso 2.3: Código a duplicado
- [ ] Caso 2.4: Mantener igual

### Día 4: Eliminar
- [ ] Caso 3.1: Eliminar OK
- [ ] Caso 3.2: Cancelar delete
- [ ] Caso 4.1: Crear filtrado

### Día 5: Responsive
- [ ] Caso 5.1: Compact
- [ ] Caso 5.2: Medium
- [ ] Caso 5.3: Expanded

### Día 6: Errores
- [ ] Caso 1.6: Error conexión en crear
- [ ] Caso 2.5: Error conexión en editar
- [ ] Caso 3.3: Error conexión en eliminar

---

## 📱 Comandos Útiles para Testing

### Abrir Logcat en Android Studio
```
View → Tool Windows → Logcat
```

### Buscar errores relacionados a inventario
```
filter: "InventoryViewModel" OR "ProductoDTO"
```

### Verificar llamadas a API
```
filter: "retrofit" OR "OkHttp"
```

---

## 📝 Plantilla de Reporte de Bug

```
REPORTE DE BUG

Caso: [# del caso de prueba]
Pasos para reproducir:
1. ...
2. ...
3. ...

Resultado esperado:
- [Lo que debería pasar]

Resultado actual:
- [Lo que pasó]

Logs:
[Pega aquí el logcat relevante]

Captura de pantalla:
[Adjunta si es posible]

Environment:
- Device: [Emulador/Físico]
- Screen size: [Compact/Medium/Expanded]
- API Level: [28/29/30...]
- APP Version: 2.0
```

---

## ✅ Checklist Final

Antes de considerar el testing completo:

- [ ] Todos los casos de CREATE pasaron
- [ ] Todos los casos de READ (búsqueda) pasaron
- [ ] Todos los casos de UPDATE pasaron
- [ ] Todos los casos de DELETE pasaron
- [ ] Validaciones funcionan correctamente
- [ ] Snackbars muestran mensajes adecuados
- [ ] Responsive funciona en 3 tamaños
- [ ] Errores de conexión se manejan
- [ ] No hay crashes
- [ ] BD refleja los cambios correctamente

---

**Versión del Testing**: 1.0  
**Fecha**: 24 de Noviembre, 2025  
**Autor**: Guía de Testing Automática

**¡Listo para comenzar el testing! 🚀**


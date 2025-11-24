# 🔍 Investigación: Agregación de Productos No Funciona

## 📋 Cambios Implementados

### 1. **Generación de IDs Correlatives**
```kotlin
// Contador comenzando en 100
private var nextProductId = 100L

private fun getNextProductId(): Long {
    return nextProductId++
}

private fun updateIdCounter() {
    val maxId = _productoDTOs.value.values.maxOfOrNull { it.id } ?: 99L
    nextProductId = maxOf(nextProductId, maxId + 1)
}
```

**Beneficios:**
- ✅ No duplica IDs
- ✅ Comienza en 100
- ✅ Se sincroniza al cargar inventario
- ✅ No depende del servidor para generar IDs

### 2. **Logging Detallado**
```kotlin
companion object {
    private const val TAG = "InventoryViewModel"
}

// En addProduct():
Log.d(TAG, "addProduct - Enviando: $newProductDto")
// ... después ...
Log.d(TAG, "addProduct - Éxito: $createdProducto")
Log.e(TAG, "addProduct - Error: ${result.exception.message}", result.exception)
```

---

## 🧪 Cómo Investigar

### Paso 1: Ver Logs en Android Studio

1. Abre Android Studio
2. Navega a: **View → Tool Windows → Logcat**
3. Busca filtro: `InventoryViewModel`
4. Intenta crear un producto

### Paso 2: Verificar lo que se envía

En Logcat busca línea como:
```
D/InventoryViewModel: addProduct - Enviando: ProductoDTO(
    id=100, 
    codigo=TEST-001, 
    nombre=Test, 
    precioActual=1500.0, 
    precio=null, 
    ...
)
```

### Paso 3: Verificar si llega respuesta

Busca línea como:
```
D/InventoryViewModel: addProduct - Éxito: ProductoDTO(
    id=75, 
    codigo=TEST-001, 
    ...
)
```

O si hay error:
```
E/InventoryViewModel: addProduct - Error: [motivo del error]
```

---

## 🎯 Posibles Causas y Soluciones

### Problema 1: Response vacía
**Síntoma:** Error en Result.Error
```
E/InventoryViewModel: addProduct - Error: Respuesta vacía
```

**Causa:** El endpoint devuelve 200 pero body es null
**Solución:** Revisar que el endpoint retorna body correcto

---

### Problema 2: Error HTTP
**Síntoma:** Log muestra HTTP error
```
E/InventoryViewModel: addProduct - Error: 400 Bad Request
```

**Causa:** Payload incorrecto o falta campo requerido
**Solución:** Verificar qué campos requiere la API

---

### Problema 3: Categoría no encontrada
**Síntoma:** categoriaDTO = null en logs
```
D/InventoryViewModel: addProduct - Enviando: ProductoDTO(
    ...
    categoria=null,
    categoriaId=null,  ← null aquí
    ...
)
```

**Causa:** El nombre de categoría no coincide
**Solución:** Verificar nombres exactos de categorías

---

## 📊 Checklist de Verificación

### Antes de crear producto:
- [ ] La app está conectada a la red
- [ ] El backend está corriendo
- [ ] Usuario está autenticado
- [ ] Las categorías se cargan correctamente

### Crear producto TEST:
- [ ] Código: `TEST-100` (formato correcto)
- [ ] Nombre: `Producto Test`
- [ ] Categoría: Selecciona una del dropdown
- [ ] Precio: `100`
- [ ] Stock: `5`
- [ ] Presiona "Agregar"

### Ver logs:
- [ ] `addProduct - Enviando: ProductoDTO(...)`
- [ ] `addProduct - Éxito: ProductoDTO(...)` O error

### Verificar en BD:
```sql
SELECT * FROM productos WHERE codigo = 'TEST-100';
-- Debe retornar 1 fila si funcionó
```

---

## 🔧 Debug con Network Inspector

### Opción 1: Android Studio Network Profiler
1. Build → Analyze APK
2. Ver requests HTTP
3. Verificar JSON enviado y recibido

### Opción 2: Logcat con nivel DEBUG
1. Logcat filter: `okhttp:V` (para ver HTTP)
2. Crear producto
3. Ver detalles del request/response

---

## 💡 Hipótesis a Verificar

### 1. El servidor no acepta campo `id`
**Solución:** Enviar `id: null` en lugar de `id: 100`
```kotlin
val newProductDto = ProductoDTO(
    id = null,  // Intentar con null
    codigo = code,
    ...
)
```

### 2. Falta un campo requerido
**Solución:** Verificar API docs de POST /api/v1/productos
```
¿Campos obligatorios?
- codigo: ✅ sí
- nombre: ✅ sí
- categoriaId: ¿sí o no?
- precio: ¿sí o no?
- stock: ✅ sí
```

### 3. El servidor rechaza la categoría
**Solución:** Intentar sin categoría primero
```kotlin
val newProductDto = ProductoDTO(
    ...
    categoria = null,
    categoriaId = null,
    ...
)
```

---

## 📝 Logs Esperados (Caso Exitoso)

```
D/InventoryViewModel: addProduct - Enviando: ProductoDTO(
    id=100, 
    codigo=TEST-100, 
    nombre=Producto Test, 
    descripcion=null, 
    stock=5, 
    precio=null, 
    precioActual=100.0, 
    categoriaId=7, 
    categoria=CategoriaDTO(id=7, nombre=Repuestos Neumáticos), 
    activo=true, 
    umbralStock=5
)

D/InventoryViewModel: addProduct - Éxito: ProductoDTO(
    id=76,  ← El servidor generó un ID diferente (OK)
    codigo=TEST-100, 
    nombre=Producto Test, 
    precio=null, 
    precioActual=100.0, 
    categoriaId=7, 
    categoria=CategoriaDTO(...)
)

D/InventoryViewModel: loadInventory - GET completado
```

---

## 🚀 Próximos Pasos

1. **Ejecutar app** y crear producto TEST
2. **Abrir Logcat** en Android Studio
3. **Buscar logs** de InventoryViewModel
4. **Compartir los logs** para análisis
5. **Verificar en BD** si producto se creó

---

**Status**: ✅ IDs Correlatives implementados + Logging agregado

**Próximo paso**: Ejecutar app, crear producto y revisar logs


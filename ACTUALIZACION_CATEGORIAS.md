# ✅ ACTUALIZACIÓN: Categorías y Pre-relleno de Datos

## 📋 Cambios Implementados

### 1. **AddProductViewModel.kt** - Carga de Categorías
```kotlin
✅ Agregado: _categorias (lista de CategoriaDTO)
✅ Agregado: _categoriasLoading (indicador de carga)
✅ Agregado: loadCategorias() - método que obtiene categorías desde API
✅ Actualizado: constructor para recibir Context
✅ Agregado: init block que llama loadCategorias()
```

**Flujo:**
```
AddProductViewModel(context)
    ↓
init { loadCategorias() }
    ↓
GET /api/v1/categorias
    ↓
_categorias = List<CategoriaDTO>
    ↓
UI recibe categorias y puede mostrar dropdown
```

---

### 2. **EditProductViewModel.kt** - Carga de Categorías
```kotlin
✅ Agregado: _categorias (lista de CategoriaDTO)
✅ Agregado: _categoriasLoading (indicador de carga)
✅ Agregado: loadCategorias() - método que obtiene categorías desde API
✅ Actualizado: constructor para recibir Context
✅ Agregado: init block que llama loadCategorias()
```

**Igual que AddProductViewModel**

---

### 3. **AddProductBottomSheet.kt** - Dropdown de Categorías
```kotlin
✅ Cambio: Campo de texto "Categoría" → ExposedDropdownMenuBox
✅ Agregado: categoriasExpanded (estado de expansión)
✅ Agregado: Importación de LocalContext
✅ Actualizado: Factory del ViewModel para pasar Context

ANTES:
OutlinedTextField(
    value = estado.categoria,
    label = { Text("Categoría") },
    ...
)

AHORA:
ExposedDropdownMenuBox(
    expanded = categoriasExpanded,
    onExpandedChange = { categoriasExpanded = !categoriasExpanded }
) {
    OutlinedTextField(
        readOnly = true,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(...) }
    )
    ExposedDropdownMenu {
        categorias.forEach { categoria ->
            DropdownMenuItem(
                text = { Text(categoria.nombre) },
                onClick = {
                    viewModel.onCategoriaChange(categoria.nombre)
                    categoriasExpanded = false
                }
            )
        }
    }
}
```

---

### 4. **EditProductScreen.kt** - Dropdown y Pre-relleno
```kotlin
✅ Cambio: Campo de texto "Categoría" → ExposedDropdownMenuBox
✅ Agregado: categoriasExpanded (estado de expansión)
✅ Agregado: Importación de LocalContext y mutableState
✅ Actualizado: Factory del ViewModel para pasar Context
✅ Ya existía: LaunchedEffect que precarga datos con loadProduct()

FLUJO DE PRE-RELLENO:
EditProductScreen(productCode = "HCOR-001")
    ↓
LaunchedEffect(productCode) {
    product = busca en lista local
    ↓
    editProductViewModel.loadProduct(product)
        ↓
        actualiza _estado con datos del producto
        ↓
        UI muestra datos precargados
}
```

---

### 5. **InventoryViewModel.kt** - Mejora en addProduct()
```kotlin
✅ Cambio: addProduct() ahora obtiene ID de categoría
```

**ANTES:**
```kotlin
val newProductDto = ProductoDTO(
    codigo = code,
    nombre = name,
    ...
    categoria = null  // ❌ NO ENVÍA CATEGORÍA
)
```

**AHORA:**
```kotlin
// Obtener la categoría completa por nombre para conseguir el ID
var categoriaDTO: CategoriaDTO? = null
val categoriasResult = inventoryRepository.getCategorias()
if (categoriasResult is Result.Success) {
    categoriaDTO = categoriasResult.data.find { it.nombre == category }
}

val newProductDto = ProductoDTO(
    codigo = code,
    nombre = name,
    ...
    categoria = categoriaDTO,  // ✅ ENVÍA CATEGORÍA CON ID
    categoriaId = categoriaDTO?.id
)
```

---

## 🔄 Flujos de Datos

### Crear Producto con Categoría
```
1. Usuario presiona FAB (+)
   ↓
2. AddProductBottomSheet se abre
   ├─ AddProductViewModel.init()
   ├─ loadCategorias()
   ├─ GET /api/v1/categorias
   └─ _categorias se llena
   ↓
3. UI muestra dropdown con categorías
   ↓
4. Usuario selecciona categoría "Herramientas"
   ├─ onCategoriaChange("Herramientas")
   └─ estado.categoria = "Herramientas"
   ↓
5. Usuario completa otros campos y presiona "Agregar"
   ↓
6. InventoryViewModel.addProduct()
   ├─ getCategorias()
   ├─ Busca: categorias.find { it.nombre == "Herramientas" }
   ├─ Obtiene: CategoriaDTO(id: 1, nombre: "Herramientas")
   └─ categoriaId = 1
   ↓
7. POST /api/v1/productos
   {
     "codigo": "HCOR-001",
     "nombre": "Broca HSS",
     "categoriaId": 1,
     ...
   }
   ↓
8. API retorna ProductoDTO con categoría asignada
   ↓
9. Snackbar: "Producto creado exitosamente"
   ↓
10. Producto aparece en lista
```

### Editar Producto
```
1. Usuario presiona ícono EDIT en ProductCard
   ↓
2. EditProductScreen(productCode = "HCOR-001")
   ├─ EditProductViewModel.init()
   ├─ loadCategorias()
   ├─ GET /api/v1/categorias
   └─ _categorias se llena
   ↓
3. LaunchedEffect(productCode)
   ├─ Busca producto en lista local
   ├─ product = InventoryItem(code: "HCOR-001", category: "Herramientas", ...)
   └─ editProductViewModel.loadProduct(product)
   ↓
4. UI muestra formulario PRECARGADO
   ├─ Código: HCOR-001 ✓
   ├─ Nombre: Broca HSS 5mm ✓
   ├─ Categoría: Herramientas ✓ (dropdown)
   ├─ Precio: 1500 ✓
   └─ Stock: 50 ✓
   ↓
5. Usuario modifica algún campo (ej: stock = 100)
   ↓
6. Usuario presiona "Guardar Cambios"
   ↓
7. InventoryViewModel.updateProduct()
   ├─ Obtiene ID del caché
   ├─ Busca CategoriaDTO por nombre
   └─ Envía PUT con categoriaId
   ↓
8. API actualiza producto
   ↓
9. Snackbar: "Producto actualizado exitosamente"
   ↓
10. Navega atrás, lista muestra cambios
```

---

## 🧪 Cómo Testear

### Test 1: Crear con Categoría Dropdown

**Pasos:**
1. Navega a "Gestión de Inventario"
2. Presiona FAB (+)
3. Espera a que se carguen categorías (verás dropdown activo)
4. Presiona el campo "Categoría"
5. Se abre lista de categorías:
   - Herramientas
   - Materiales
   - Equipos
   - Consumibles
6. Selecciona "Herramientas"
7. Completa otros campos
8. Presiona "Agregar"

**Resultado Esperado:**
- ✅ Categoría se selecciona desde dropdown
- ✅ Campo muestra "Herramientas"
- ✅ Producto se crea exitosamente
- ✅ Aparece en lista con categoría correcta

**Si no funciona:**
- ❌ Verificar que API devuelve categorías en GET /api/v1/categorias
- ❌ Verificar que API acepta categoriaId en POST

---

### Test 2: Editar con Datos Precargados

**Pasos:**
1. Navega a "Gestión de Inventario"
2. Presiona ícono EDIT en cualquier producto
3. Verifica que los datos están precargados:
   - Código: (mismo código)
   - Nombre: (mismo nombre)
   - Categoría: (categoría correcta en dropdown)
   - Precio: (mismo precio)
   - Stock: (mismo stock)
4. Cambia el stock a 100
5. Presiona "Guardar Cambios"

**Resultado Esperado:**
- ✅ Todos los datos están precargados
- ✅ Categoría muestra en dropdown
- ✅ Cambios se guardan
- ✅ Navega atrás automáticamente

**Si no funciona:**
- ❌ Verificar que EditProductViewModel.loadProduct() está siendo llamado
- ❌ Verificar que el LaunchedEffect se ejecuta
- ❌ Verificar que la lista local tiene el producto

---

### Test 3: Verificar Categoría se Envía a API

**Precondiciones:**
- Abrir Developer Tools / Network tab
- O agregar logs en logcat

**Pasos:**
1. Crear nuevo producto con categoría "Herramientas"
2. Observar el POST request:
   ```
   POST /api/v1/productos
   {
     "codigo": "HCOR-001",
     "nombre": "Broca",
     "categoriaId": 1,  // ← DEBE ESTAR PRESENTE
     ...
   }
   ```

**Resultado Esperado:**
- ✅ categoriaId está en el request
- ✅ categoriaId es el número correcto
- ✅ API retorna OK

---

## 🐛 Posibles Problemas

| Síntoma | Causa | Solución |
|---------|-------|----------|
| Dropdown vacío | Categorías no cargan | Verificar GET /api/v1/categorias |
| Producto no se crea | categoriaId es null | Verificar que categorías existen |
| Datos no precargados | LaunchedEffect no se ejecuta | Verificar product no es null |
| Categoría incorrecta | No coincide el nombre | Asegurar nombres iguales |
| Error al guardar | categoriaId no enviado | Verificar InventoryViewModel |

---

## 📱 Arquitectura Actualizada

```
AddProductBottomSheet
    ├─ AddProductViewModel (con Context)
    │  ├─ loadCategorias()
    │  ├─ _categorias: List<CategoriaDTO>
    │  └─ onCategoriaChange(nombre: String)
    │
    └─ ExposedDropdownMenuBox
       ├─ mostra lista de categorías.nombre
       └─ al seleccionar, llama onCategoriaChange()

EditProductScreen
    ├─ EditProductViewModel (con Context)
    │  ├─ loadCategorias()
    │  ├─ _categorias: List<CategoriaDTO>
    │  ├─ loadProduct(InventoryItem)
    │  └─ onCategoriaChange(nombre: String)
    │
    ├─ LaunchedEffect
    │  └─ busca producto en lista
    │     └─ llama editProductViewModel.loadProduct()
    │
    └─ ExposedDropdownMenuBox
       ├─ estado.categoria = nombre de categoría
       └─ muestra en dropdown

InventoryViewModel
    ├─ addProduct()
    │  ├─ obtiene categoriaId por nombre
    │  ├─ envía POST con categoriaId
    │  └─ producto se crea con categoría
    │
    └─ updateProduct()
       ├─ obtiene categoriaId por nombre
       ├─ envía PUT con categoriaId
       └─ producto se actualiza con categoría
```

---

## ✅ Checklist

- [x] Categorías cargan desde API
- [x] Dropdown muestra categorías
- [x] Edición pre-rellena datos
- [x] Categoría se envía en POST/PUT
- [x] categoriaId se obtiene correctamente
- [x] Producto se crea con categoría
- [x] Producto se actualiza con categoría

---

## 🚀 Próximos Pasos (Opcional)

1. Agregar Snackbars para feedback visual
2. Mostrar loading spinner mientras se cargan categorías
3. Manejar casos donde no hay categorías
4. Agregar crear categoría desde formulario

---

**Versión**: 2.1
**Cambios**: Categorías dinámicas + Pre-relleno
**Estado**: ✅ LISTO PARA TESTING


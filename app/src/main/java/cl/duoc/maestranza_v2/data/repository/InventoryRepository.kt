package cl.duoc.maestranza_v2.data.repository

import cl.duoc.maestranza_v2.data.model.ProductoDTO
import cl.duoc.maestranza_v2.data.model.CategoriaDTO
import cl.duoc.maestranza_v2.data.remote.ApiClient

class InventoryRepository(private val apiClient: ApiClient) {

    private val api = apiClient.service

    // ==================== PRODUCTOS ====================

    /**
     * Obtener todos los productos
     */
    suspend fun getProductos(): Result<List<ProductoDTO>> {
        return try {
            val response = api.getProductos()
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                Result.Success(productos)
            } else {
                Result.Error(Exception("Error al obtener productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener producto por ID
     */
    suspend fun getProductoById(id: Long): Result<ProductoDTO> {
        return try {
            val response = api.getProductoById(id)
            if (response.isSuccessful) {
                val producto = response.body()
                if (producto != null) {
                    Result.Success(producto)
                } else {
                    Result.Error(Exception("Producto no encontrado"))
                }
            } else {
                Result.Error(Exception("Error al obtener producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Crear nuevo producto
     */
    suspend fun createProducto(producto: ProductoDTO): Result<ProductoDTO> {
        return try {
            val response = api.createProducto(producto)
            if (response.isSuccessful) {
                val nuevoProducto = response.body()
                if (nuevoProducto != null) {
                    Result.Success(nuevoProducto)
                } else {
                    Result.Error(Exception("Respuesta vacía al crear producto"))
                }
            } else {
                Result.Error(Exception("Error al crear producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Actualizar producto
     */
    suspend fun updateProducto(id: Long, producto: ProductoDTO): Result<ProductoDTO> {
        return try {
            val response = api.updateProducto(id, producto)
            if (response.isSuccessful) {
                val productoActualizado = response.body()
                if (productoActualizado != null) {
                    Result.Success(productoActualizado)
                } else {
                    Result.Error(Exception("Respuesta vacía al actualizar producto"))
                }
            } else {
                Result.Error(Exception("Error al actualizar producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Eliminar producto
     */
    suspend fun deleteProducto(id: Long): Result<Boolean> {
        return try {
            val response = api.deleteProducto(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Error al eliminar producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener productos con stock bajo
     */
    suspend fun getProductosConStockBajo(): Result<List<ProductoDTO>> {
        return try {
            val response = api.getProductosConStockBajo()
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                Result.Success(productos)
            } else {
                Result.Error(Exception("Error al obtener productos con stock bajo: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener productos agotados
     */
    suspend fun getProductosAgotados(): Result<List<ProductoDTO>> {
        return try {
            val response = api.getProductosAgotados()
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                Result.Success(productos)
            } else {
                Result.Error(Exception("Error al obtener productos agotados: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Buscar productos por término
     */
    suspend fun buscarProductos(termino: String): Result<List<ProductoDTO>> {
        return try {
            val response = api.buscarProductos(termino)
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                Result.Success(productos)
            } else {
                Result.Error(Exception("Error al buscar productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Actualizar stock de un producto
     */
    suspend fun actualizarStock(id: Long, nuevoStock: Int): Result<ProductoDTO> {
        return try {
            val response = api.actualizarStock(id, nuevoStock)
            if (response.isSuccessful) {
                val producto = response.body()
                if (producto != null) {
                    Result.Success(producto)
                } else {
                    Result.Error(Exception("Respuesta vacía al actualizar stock"))
                }
            } else {
                Result.Error(Exception("Error al actualizar stock: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Desactivar producto
     */
    suspend fun desactivarProducto(id: Long): Result<ProductoDTO> {
        return try {
            val response = api.desactivarProducto(id)
            if (response.isSuccessful) {
                val producto = response.body()
                if (producto != null) {
                    Result.Success(producto)
                } else {
                    Result.Error(Exception("Respuesta vacía al desactivar"))
                }
            } else {
                Result.Error(Exception("Error al desactivar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Reactivar producto
     */
    suspend fun reactivarProducto(id: Long): Result<ProductoDTO> {
        return try {
            val response = api.reactivarProducto(id)
            if (response.isSuccessful) {
                val producto = response.body()
                if (producto != null) {
                    Result.Success(producto)
                } else {
                    Result.Error(Exception("Respuesta vacía al reactivar"))
                }
            } else {
                Result.Error(Exception("Error al reactivar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ==================== CATEGORÍAS ====================

    /**
     * Obtener todas las categorías
     */
    suspend fun getCategorias(): Result<List<CategoriaDTO>> {
        return try {
            val response = api.getCategorias()
            if (response.isSuccessful) {
                val categorias = response.body() ?: emptyList()
                Result.Success(categorias)
            } else {
                Result.Error(Exception("Error al obtener categorías: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener categoría por ID
     */
    suspend fun getCategoriaById(id: Long): Result<CategoriaDTO> {
        return try {
            val response = api.getCategoriaById(id)
            if (response.isSuccessful) {
                val categoria = response.body()
                if (categoria != null) {
                    Result.Success(categoria)
                } else {
                    Result.Error(Exception("Categoría no encontrada"))
                }
            } else {
                Result.Error(Exception("Error al obtener categoría: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Crear nueva categoría
     */
    suspend fun createCategoria(categoria: CategoriaDTO): Result<CategoriaDTO> {
        return try {
            val response = api.createCategoria(categoria)
            if (response.isSuccessful) {
                val nuevaCategoria = response.body()
                if (nuevaCategoria != null) {
                    Result.Success(nuevaCategoria)
                } else {
                    Result.Error(Exception("Respuesta vacía al crear categoría"))
                }
            } else {
                Result.Error(Exception("Error al crear categoría: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Actualizar categoría
     */
    suspend fun updateCategoria(id: Long, categoria: CategoriaDTO): Result<CategoriaDTO> {
        return try {
            val response = api.updateCategoria(id, categoria)
            if (response.isSuccessful) {
                val categoriaActualizada = response.body()
                if (categoriaActualizada != null) {
                    Result.Success(categoriaActualizada)
                } else {
                    Result.Error(Exception("Respuesta vacía al actualizar"))
                }
            } else {
                Result.Error(Exception("Error al actualizar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Eliminar categoría
     */
    suspend fun deleteCategoria(id: Long): Result<Boolean> {
        return try {
            val response = api.deleteCategoria(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Error al eliminar categoría: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener productos por categoría
     */
    suspend fun getProductosByCategoria(categoriaId: Long): Result<List<ProductoDTO>> {
        return try {
            val response = api.getProductosByCategoria(categoriaId)
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                Result.Success(productos)
            } else {
                Result.Error(Exception("Error al obtener productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}


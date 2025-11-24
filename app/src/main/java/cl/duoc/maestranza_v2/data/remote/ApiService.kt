package cl.duoc.maestranza_v2.data.remote

import cl.duoc.maestranza_v2.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ==================== AUTENTICACIÓN ====================

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<JwtResponse>

    @POST("api/auth/registro")
    suspend fun signup(@Body request: SignupRequest): Response<MessageResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<MessageResponse>

    @GET("api/auth/verify")
    suspend fun verify(): Response<Any>

    @GET("api/auth/validate-token")
    suspend fun validateToken(): Response<Any>

    // ==================== PRODUCTOS ====================

    @GET("api/v1/productos")
    suspend fun getProductos(): Response<List<ProductoDTO>>

    @GET("api/v1/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): Response<ProductoDTO>

    @POST("api/v1/productos")
    suspend fun createProducto(@Body producto: ProductoDTO): Response<ProductoDTO>

    @PUT("api/v1/productos/{id}")
    suspend fun updateProducto(@Path("id") id: Long, @Body producto: ProductoDTO): Response<ProductoDTO>

    @DELETE("api/v1/productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Long): Response<Map<String, String>>

    @GET("api/v1/productos/categoria/{categoriaId}")
    suspend fun getProductosByCategoria(@Path("categoriaId") categoriaId: Long): Response<List<ProductoDTO>>

    @GET("api/v1/productos/stock-bajo")
    suspend fun getProductosConStockBajo(): Response<List<ProductoDTO>>

    @GET("api/v1/productos/agotados")
    suspend fun getProductosAgotados(): Response<List<ProductoDTO>>

    @GET("api/v1/productos/buscar")
    suspend fun buscarProductos(@Query("termino") termino: String): Response<List<ProductoDTO>>

    @PATCH("api/v1/productos/{id}/stock")
    suspend fun actualizarStock(@Path("id") id: Long, @Query("nuevoStock") nuevoStock: Int): Response<ProductoDTO>

    @PATCH("api/v1/productos/{id}/desactivar")
    suspend fun desactivarProducto(@Path("id") id: Long): Response<ProductoDTO>

    @PATCH("api/v1/productos/{id}/reactivar")
    suspend fun reactivarProducto(@Path("id") id: Long): Response<ProductoDTO>

    // ==================== CATEGORÍAS ====================

    @GET("api/v1/categorias")
    suspend fun getCategorias(): Response<List<CategoriaDTO>>

    @GET("api/v1/categorias/{id}")
    suspend fun getCategoriaById(@Path("id") id: Long): Response<CategoriaDTO>

    @POST("api/v1/categorias")
    suspend fun createCategoria(@Body categoria: CategoriaDTO): Response<CategoriaDTO>

    @PUT("api/v1/categorias/{id}")
    suspend fun updateCategoria(@Path("id") id: Long, @Body categoria: CategoriaDTO): Response<CategoriaDTO>

    @DELETE("api/v1/categorias/{id}")
    suspend fun deleteCategoria(@Path("id") id: Long): Response<Unit>

    @GET("api/v1/categorias/nombre/{nombre}")
    suspend fun getCategoriaPorNombre(@Path("nombre") nombre: String): Response<CategoriaDTO>

    // ==================== USUARIOS ====================

    @GET("api/v1/usuarios")
    suspend fun getUsuarios(): Response<List<UsuarioDTO>>

    @GET("api/v1/usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Long): Response<UsuarioDTO>

    @POST("api/v1/usuarios")
    suspend fun createUsuario(@Body usuario: CrearUsuarioDTO): Response<Map<String, Any>>

    @PATCH("api/v1/usuarios/{id}")
    suspend fun updateUsuario(@Path("id") id: Long, @Body usuario: ActualizarUsuarioDTO): Response<Map<String, Any>>

    @DELETE("api/v1/usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Long): Response<Map<String, Any>>

    @GET("api/v1/usuarios/username/{username}")
    suspend fun getUsuarioPorUsername(@Path("username") username: String): Response<UsuarioDTO>

    @PATCH("api/v1/usuarios/{id}/activar")
    suspend fun activarUsuario(@Path("id") id: Long): Response<Map<String, Any>>

    @PATCH("api/v1/usuarios/{id}/desactivar")
    suspend fun desactivarUsuario(@Path("id") id: Long): Response<Map<String, Any>>

    // ==================== MOVIMIENTOS ====================

    @GET("api/v1/movimientos")
    suspend fun getMovimientos(): Response<List<MovimientoDTO>>

    @GET("api/v1/movimientos/{id}")
    suspend fun getMovimientoById(@Path("id") id: Long): Response<MovimientoDTO>

    @POST("api/v1/movimientos")
    suspend fun createMovimiento(@Body movimiento: CrearMovimientoRequest): Response<MovimientoDTO>

    @PUT("api/v1/movimientos/{id}")
    suspend fun updateMovimiento(@Path("id") id: Long, @Body movimiento: MovimientoDTO): Response<MovimientoDTO>

    @DELETE("api/v1/movimientos/{id}")
    suspend fun deleteMovimiento(@Path("id") id: Long): Response<Unit>

    @GET("api/v1/movimientos/tipo/{tipo}")
    suspend fun getMovimientosPorTipo(@Path("tipo") tipo: String): Response<List<MovimientoDTO>>

    @GET("api/v1/movimientos/producto/{productoId}")
    suspend fun getMovimientosPorProducto(@Path("productoId") productoId: Long): Response<List<MovimientoDTO>>

    @GET("api/v1/movimientos/usuario/{usuarioId}")
    suspend fun getMovimientosPorUsuario(@Path("usuarioId") usuarioId: Long): Response<List<MovimientoDTO>>

    @GET("api/v1/movimientos/filtros")
    suspend fun buscarMovimientosConFiltros(
        @Query("productoId") productoId: Long? = null,
        @Query("usuarioId") usuarioId: Long? = null,
        @Query("tipo") tipo: String? = null,
        @Query("fechaInicio") fechaInicio: String? = null,
        @Query("fechaFin") fechaFin: String? = null
    ): Response<List<MovimientoDTO>>

    @GET("api/v1/movimientos/fechas")
    suspend fun getMovimientosPorRangoFechas(
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFin") fechaFin: String
    ): Response<List<MovimientoDTO>>

    @GET("api/v1/movimientos/buscar/codigo")
    suspend fun buscarMovimientosPorCodigoProducto(@Query("productoCodigo") codigo: String): Response<List<MovimientoDTO>>

    @GET("api/v1/movimientos/buscar/nombre")
    suspend fun buscarMovimientosPorNombreProducto(@Query("productoNombre") nombre: String): Response<List<MovimientoDTO>>

    // ==================== ROLES ====================

    @GET("api/v1/roles")
    suspend fun getRoles(): Response<List<RolDTO>>

    @GET("api/v1/roles/{id}")
    suspend fun getRolById(@Path("id") id: Int): Response<RolDTO>

    @GET("api/v1/roles/nombre/{nombre}")
    suspend fun getRolPorNombre(@Path("nombre") nombre: String): Response<RolDTO>

    @POST("api/v1/roles")
    suspend fun createRol(@Body rol: RolDTO): Response<RolDTO>

    @PUT("api/v1/roles/{id}")
    suspend fun updateRol(@Path("id") id: Int, @Body rol: RolDTO): Response<RolDTO>

    @DELETE("api/v1/roles/{id}")
    suspend fun deleteRol(@Path("id") id: Int): Response<Unit>

    // ==================== ALERTAS ====================

    @GET("api/v1/alertas")
    suspend fun getAlertas(): Response<List<AlertaDTO>>

    @GET("api/v1/alertas/{id}")
    suspend fun getAlertaById(@Path("id") id: Long): Response<AlertaDTO>

    @GET("api/v1/alertas/activas")
    suspend fun getAlertasActivas(): Response<List<AlertaDTO>>

    @GET("api/v1/alertas/criticas")
    suspend fun getAlertasCriticas(): Response<List<AlertaDTO>>

    @GET("api/v1/alertas/hoy")
    suspend fun getAlertasDelDia(): Response<List<AlertaDTO>>

    @GET("api/v1/alertas/ultimas/{limite}")
    suspend fun getUltimasAlertas(@Path("limite") limite: Int): Response<List<AlertaDTO>>

    @GET("api/v1/alertas/producto/{productoId}")
    suspend fun getAlertasPorProducto(@Path("productoId") productoId: Long): Response<List<AlertaDTO>>
}


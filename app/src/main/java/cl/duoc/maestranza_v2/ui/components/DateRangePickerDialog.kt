package cl.duoc.maestranza_v2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun DateRangePickerDialog(
    title: String,
    subtitle: String? = null,
    currentDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedYear by remember { mutableStateOf(currentDate?.year ?: LocalDate.now().year) }
    var selectedMonth by remember { mutableStateOf(currentDate?.monthValue ?: LocalDate.now().monthValue) }
    var selectedDay by remember { mutableStateOf(currentDate?.dayOfMonth ?: LocalDate.now().dayOfMonth) }

    var expandedDay by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }

    val monthNames = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    val monthAbbrev = listOf(
        "Ene", "Feb", "Mar", "Abr", "May", "Jun",
        "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.95f),
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Preview de la fecha seleccionada
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = LocalDate.of(selectedYear, selectedMonth, minOf(selectedDay, getDaysInMonth(selectedYear, selectedMonth)))
                            .format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }

                // Selectores: Día, Mes, Año con Dropdowns
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Selector de Día
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Día",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { expandedDay = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("%02d".format(minOf(selectedDay, getDaysInMonth(selectedYear, selectedMonth))))
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }

                            DropdownMenu(
                                expanded = expandedDay,
                                onDismissRequest = { expandedDay = false },
                                modifier = Modifier.fillMaxWidth(0.3f)
                            ) {
                                repeat(getDaysInMonth(selectedYear, selectedMonth)) { day ->
                                    DropdownMenuItem(
                                        text = { Text("%02d".format(day + 1)) },
                                        onClick = {
                                            selectedDay = day + 1
                                            expandedDay = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Selector de Mes
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mes",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { expandedMonth = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(monthAbbrev[selectedMonth - 1])
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }

                            DropdownMenu(
                                expanded = expandedMonth,
                                onDismissRequest = { expandedMonth = false },
                                modifier = Modifier.fillMaxWidth(0.35f)
                            ) {
                                monthAbbrev.forEachIndexed { index, monthName ->
                                    DropdownMenuItem(
                                        text = { Text(monthName) },
                                        onClick = {
                                            selectedMonth = index + 1
                                            expandedMonth = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Selector de Año
                    Column(
                        modifier = Modifier.weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Año",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { expandedYear = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(selectedYear.toString())
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }

                            DropdownMenu(
                                expanded = expandedYear,
                                onDismissRequest = { expandedYear = false }
                            ) {
                                // Mostrar años desde 10 años atrás hasta 10 años adelante
                                val currentYear = LocalDate.now().year
                                for (year in (currentYear - 10)..(currentYear + 10)) {
                                    DropdownMenuItem(
                                        text = { Text(year.toString()) },
                                        onClick = {
                                            selectedYear = year
                                            expandedYear = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalDay = minOf(selectedDay, getDaysInMonth(selectedYear, selectedMonth))
                    onDateSelected(LocalDate.of(selectedYear, selectedMonth, finalDay))
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Obtiene la cantidad de días en un mes específico
 */
private fun getDaysInMonth(year: Int, month: Int): Int {
    return YearMonth.of(year, month).lengthOfMonth()
}



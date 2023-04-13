package ru.mephi.alumniclub.app.service.impl

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.repository.user.MentorDao
import ru.mephi.alumniclub.app.model.dto.excel.ExcelFieldGetter
import ru.mephi.alumniclub.app.model.dto.excel.getters.eventParticipationFieldGetters
import ru.mephi.alumniclub.app.model.dto.excel.getters.meetingParticipationFieldGetters
import ru.mephi.alumniclub.app.model.dto.excel.getters.userFieldGetters
import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.service.ExcelService
import ru.mephi.alumniclub.app.service.PublicationService
import ru.mephi.alumniclub.app.service.UserService
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.*

@Service
class ExcelServiceImpl(
    private val userService: UserService,
    private val publicationService: PublicationService
) : ExcelService {


    /**
     * Create a cell in an Excel sheet and write a value to it
     */
    fun <T> Row.writeToCell(columnNumber: Int, value: T, style: CellStyle) {
        val cell = this.createCell(columnNumber)
        when (value) {
            is Boolean -> cell.setCellValue(if (value) "Да" else "Нет")
            is Double -> cell.setCellValue(value)
            is LocalDateTime -> cell.setCellValue(value.toString().replace("T", " "))
            is List<*> -> cell.setCellValue(value.joinToString("\n"))
            else -> cell.setCellValue(value?.toString() ?: "")
        }
        cell.cellStyle = style
    }

    /**
     * Apply style for header row in sheet
     */
    private fun setHeaderStyle(workbook: XSSFWorkbook): CellStyle {
        val font = workbook.createFont().apply {
            setFontHeight(16.0)
            bold = true
        }
        return workbook.createCellStyle().apply { setFont(font) }
    }

    /**
     * Apply style for common cell
     */
    private fun setCellStyle(workbook: XSSFWorkbook): CellStyle {
        val font = workbook.createFont().apply { setFontHeight(14.0) }
        return workbook.createCellStyle().apply { setFont(font) }
    }

    /**
     * Create styled header and fill with titles
     */
    private fun <T> prepareSheet(sheet: XSSFSheet, getters: List<ExcelFieldGetter<T>>) {
        val headerStyle = setHeaderStyle(sheet.workbook)
        val headerRow = sheet.createRow(0)
        getters.forEachIndexed { columnIndex, getter ->
            headerRow.writeToCell(columnIndex, getter.name, headerStyle)
            sheet.autoSizeColumn(columnIndex)
        }
    }

    /**
     * Write entities one at a time to a line.
     * For each row in the sheet, the getters are used to get the value of the entity.
     * Retrieved value is written to the styled cell.
     */
    private fun <T> writeData(sheet: XSSFSheet, getters: List<ExcelFieldGetter<T>>, entities: Iterable<T>) {
        val cellStyle = setCellStyle(sheet.workbook)
        entities.forEachIndexed { rowIndex, entity ->
            val row = sheet.createRow(rowIndex + 1)
            getters.forEachIndexed { columnIndex, getter ->
                row.writeToCell(columnIndex, getter.getValue(entity), cellStyle)
            }
        }
    }

    /**
     * Convert XSSFWorkbook into ByteArrayResource
     */
    private fun XSSFWorkbook.toByteArrayResource(): ByteArrayResource {
        val outputStream = ByteArrayOutputStream()
        this.write(outputStream)
        this.close()
        outputStream.close()
        return ByteArrayResource(outputStream.toByteArray())
    }

    /**
     * Create sheet filled with entities and export as ByteArrayResource
     */
    private fun <T> export(
        title: String,
        getters: List<ExcelFieldGetter<T>>,
        entities: Iterable<T>
    ): ByteArrayResource {
        val sheet = XSSFWorkbook().createSheet(title)
        prepareSheet(sheet, getters)
        writeData(sheet, getters, entities)
        return sheet.workbook.toByteArrayResource()
    }

    /**
     * Create sheet filled with users and export as ByteArrayResource
     */
    override fun exportUsers() = export("Пользователи", userFieldGetters(), userService.export())

    /**
     * Create sheet filled with event participants and export as ByteArrayResource
     */
    override fun exportEventParticipants(eventId: UUID): ByteArrayResource {
        val event = publicationService.findEventEntity(eventId, ignoreChecks = true)
        val getters = eventParticipationFieldGetters() +
                if (event.externalRegistrationLink != null) meetingParticipationFieldGetters() else emptyList()
        return export("Участники мероприятия", getters, publicationService.exportParticipants(eventId))
    }
}

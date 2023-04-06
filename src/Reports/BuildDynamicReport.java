package Reports;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.*;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import ar.com.fdvs.dj.util.customexpression.RecordsInPageCustomExpression;
import models.TarifaSalarial;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class BuildDynamicReport {

    ReportesController reportesController;

    public DynamicReport createControlPresupReport(ReportesController reportes, int niveles, boolean tab, int temp) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        if (temp == 1) {

            Integer margin = (20);
            drb
                    .setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setGrandTotalLegend("Total Costos Directos")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8Letter.jrxml");
        } else if (temp == 2) {
            Integer margin = (20);
            drb
                    .setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    // .setPrintColumnNames(true)
                    .setGrandTotalLegend("Total Costos Directos")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LetterPie.jrxml");
        }

        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        /**
         * grupos en el reporte
         */

        AbstractColumn colempresa = ColumnBuilder.getNew()
                .setColumnProperty("empresaName", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn colzona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn colobjeto = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn colnivel = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn colespecialidad = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn colsubespecialidad = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        /**
         * Contenido de la table
         */

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Código").setWidth(15)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(40)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacostUnitario = ColumnBuilder.getNew()
                .setColumnProperty("costUnitario", Double.class.getName())
                .setTitle("C.Unit").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn materiales = ColumnBuilder.getNew()
                .setColumnProperty("materiales", Double.class.getName())
                .setTitle("C. Mater").setWidth(17).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
        AbstractColumn mano = ColumnBuilder.getNew()
                .setColumnProperty("mano", Double.class.getName())
                .setTitle("C. Mano").setWidth(17).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
        AbstractColumn equipo = ColumnBuilder.getNew()
                .setColumnProperty("equipo", Double.class.getName())
                .setTitle("C. Equip").setWidth(17).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatotal = ColumnBuilder.getNew()
                .setColumnProperty("costTotal", Double.class.getName())
                .setTitle("C. Total").setWidth(17).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        // DJGroupLabel glabel1 = new DJGroupLabel("Material", glabelStyle);
        // DJGroupLabel glabel2 = new DJGroupLabel("Mano Obra", glabelStyle);
        //DJGroupLabel glabel3 = new DJGroupLabel("Equipos", glabelStyle);
        //DJGroupLabel glabel4 = new DJGroupLabel("Costo Total", glabelStyle);

        drb.addColumn(columnacode);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        drb.addColumn(columnacant);
        drb.addColumn(columnacostUnitario);
        drb.addColumn(materiales);
        drb.addColumn(mano);
        drb.addColumn(equipo);
        drb.addColumn(columnatotal);


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();
        drb.addColumn(id);
        drb.addGroup(g1Id);

        if (!tab) {
            drb.addField("bodyConceptos", ArrayList.class.getName());
            DynamicReport drFooterSubreport = createFooterSubreport(temp);
            drb.addSubreportInGroupFooter(1, drFooterSubreport, new ClassicLayoutManager(),
                    "bodyConceptos", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

            //DynamicReport drFooterSubreport3 = createResumenCostos(1);
            //drb.addConcatenatedReport(drFooterSubreport3, new ClassicLayoutManager(), "resumen", DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);


        } else {
            drb.addGlobalFooterVariable(materiales, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(mano, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(equipo, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null);
        }


        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresaName");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Especialidad: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Subespecialidad: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);

        GroupBuilder gb1 = new GroupBuilder();
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) colempresa)
                .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();

        GroupBuilder gb2 = new GroupBuilder();
        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) colzona)
                .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Z)
                .setFooterVariablesHeight(12)
                .build();

        GroupBuilder gb3 = new GroupBuilder();
        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) colobjeto)
                .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3O)
                .setFooterVariablesHeight(12)
                .build();

        GroupBuilder gb4 = new GroupBuilder();
        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) colnivel)
                .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3N)
                .setFooterVariablesHeight(12)
                .build();

        GroupBuilder gb5 = new GroupBuilder();
        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) colespecialidad)
                .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Es)
                .setFooterVariablesHeight(12)
                .build();

        GroupBuilder gb6 = new GroupBuilder();
        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) colsubespecialidad)
                .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3S)
                .setFooterVariablesHeight(12)
                .build();

        if (niveles == 1) {
            drb.addColumn(colempresa);
            drb.addGroup(g1);


        } else if (niveles == 2) {
            drb.addColumn(colzona);
            drb.addColumn(colempresa);


            drb.addGroup(g1);
            drb.addGroup(g2);


        } else if (niveles == 3) {
            drb.addColumn(colempresa);
            drb.addColumn(colzona);
            drb.addColumn(colobjeto);


            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);


        } else if (niveles == 4) {
            drb.addColumn(colempresa);
            drb.addColumn(colzona);
            drb.addColumn(colobjeto);
            drb.addColumn(colnivel);


            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);


        } else if (niveles == 5) {
            drb.addColumn(colempresa);
            drb.addColumn(colzona);
            drb.addColumn(colobjeto);
            drb.addColumn(colnivel);
            drb.addColumn(colespecialidad);

            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);

        } else if (niveles == 6) {
            drb.addColumn(colempresa);
            drb.addColumn(colzona);
            drb.addColumn(colobjeto);
            drb.addColumn(colnivel);
            drb.addColumn(colespecialidad);
            drb.addColumn(colsubespecialidad);

            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);

        } else if (niveles == 15) {
            drb.addColumn(colempresa);

            drb.addGroup(g1);

            drb.addColumn(colespecialidad);

            drb.addGroup(g5);


        } else if (niveles == 125) {
            drb.addColumn(colempresa);

            drb.addGroup(g1);

            drb.addColumn(colzona);

            drb.addGroup(g2);
            drb.addColumn(colespecialidad);

            drb.addGroup(g5);


        } else if (niveles == 1235) {
            drb.addColumn(colempresa);

            drb.addGroup(g1);

            drb.addColumn(colzona);

            drb.addGroup(g2);

            drb.addColumn(colobjeto);

            drb.addGroup(g3);

            drb.addColumn(colespecialidad);

            drb.addGroup(g5);


        } else if (niveles == 12345) {
            drb.addColumn(colempresa);

            drb.addGroup(g1);

            drb.addColumn(colzona);

            drb.addGroup(g2);

            drb.addColumn(colobjeto);

            drb.addGroup(g3);

            drb.addColumn(colnivel);


            drb.addGroup(g4);

            drb.addColumn(colespecialidad);

            drb.addGroup(g5);


        } else if (niveles == 156) {
            drb.addColumn(colempresa);

            drb.addGroup(g1);
            drb.addColumn(colespecialidad);

            drb.addGroup(g5);
            drb.addColumn(colsubespecialidad);

            drb.addGroup(g6);


        } else if (niveles == 1256) {
            drb.addColumn(colempresa);

            drb.addGroup(g1);
            drb.addColumn(colzona);

            drb.addGroup(g2);
            drb.addColumn(colespecialidad);

            drb.addGroup(g5);
            drb.addColumn(colsubespecialidad);

            drb.addGroup(g6);


        } else if (niveles == 12356) {
            drb.addColumn(colempresa);

            drb.addGroup(g1);
            drb.addColumn(colzona);

            drb.addGroup(g2);
            drb.addColumn(colobjeto);

            drb.addGroup(g3);
            drb.addColumn(colespecialidad);

            drb.addGroup(g5);
            drb.addColumn(colsubespecialidad);

            drb.addGroup(g6);


        } else if (niveles == 123456) {
            drb.addColumn(colempresa);

            drb.addGroup(g1);
            drb.addColumn(colzona);

            drb.addGroup(g2);
            drb.addColumn(colobjeto);

            drb.addGroup(g3);
            drb.addColumn(colnivel);

            drb.addGroup(g4);
            drb.addColumn(colespecialidad);

            drb.addGroup(g5);
            drb.addColumn(colsubespecialidad);
            drb.addGroup(g6);


        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }


    public DynamicReport createControlPresupReportUORV(ReportesController reportes, int niveles, int temp, boolean tab) throws ClassNotFoundException {

        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style detailStyle1 = new Style();
        detailStyle1.setVerticalAlign(VerticalAlign.TOP);
        detailStyle1.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStyle1.setFont(Font.ARIAL_MEDIUM_BOLD);


        Style detail2Style = new Style();
        detail2Style.setVerticalAlign(VerticalAlign.TOP);
        detail2Style.setHorizontalAlign(HorizontalAlign.LEFT);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerStyle1 = new Style();
        headerStyle1.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle1.setBorderBottom(Border.PEN_1_POINT());
        headerStyle1.setBorderTop(Border.PEN_1_POINT());
        headerStyle1.setBackgroundColor(Color.white);
        headerStyle1.setTextColor(Color.DARK_GRAY);
        headerStyle1.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle1.setTransparency(Transparency.OPAQUE);


        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.TOP);

        DynamicReportBuilder drb = new DynamicReportBuilder();

        if (temp == 1) {

            Integer margin = new Integer(20);
            drb
                    .setTitleStyle(titleStyle)
                    .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    //  .setPrintColumnNames(true)
                    .setGrandTotalLegend("Costos Directos Obra")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LandScape.jrxml");
        } else if (temp == 2) {
            Integer margin = new Integer(20);
            drb
                    .setTitleStyle(titleStyle)
                    .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    //   .setPrintColumnNames(true)
                    .setGrandTotalLegend("Costos Directos Obra")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LetterPie.jrxml");
        }
        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopUnidad = ColumnBuilder.getNew()
                .setColumnProperty("unidad", String.class.getName())
                .setTitle("UO: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Código").setWidth(23)
                .setStyle(detail2Style).setHeaderStyle(headerStyle1)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(45)
                .setStyle(detail2Style).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamat = ColumnBuilder.getNew()
                .setColumnProperty("material", Double.class.getName())
                .setTitle("Mat.").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamano = ColumnBuilder.getNew()
                .setColumnProperty("mano", Double.class.getName())
                .setTitle("Mano").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaequp = ColumnBuilder.getNew()
                .setColumnProperty("equip", Double.class.getName())
                .setTitle("Equip.").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacostMat = ColumnBuilder.getNew()
                .setColumnProperty("costMat", Double.class.getName())
                .setTitle("C.Mat").setWidth(17).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostMano = ColumnBuilder.getNew()
                .setColumnProperty("costMano", Double.class.getName())
                .setTitle("C.Man").setWidth(16).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostEqup = ColumnBuilder.getNew()
                .setColumnProperty("costEquip", Double.class.getName())
                .setTitle("Equip").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaTotal = ColumnBuilder.getNew()
                .setColumnProperty("total", Double.class.getName())
                .setTitle("Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();


        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT)
                .setVerticalAlign(VerticalAlign.BOTTOM)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Especialidad: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Subespecialidad: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabelUn = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Unidad de Obra: ";
            }
        }, glabelStyle2, null);

        // DJGroupLabel glabel1 = new DJGroupLabel("Material", glabelStyle);
        //DJGroupLabel glabel2 = new DJGroupLabel("Mano Obra", glabelStyle);
        //DJGroupLabel glabel3 = new DJGroupLabel("Equipos", glabelStyle);
        //DJGroupLabel glabel4 = new DJGroupLabel("Costo Total", glabelStyle);


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, detailStyle1, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, detailStyle1, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Z)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, detailStyle1, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3O)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, detailStyle1, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3N)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, detailStyle1, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Es)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, detailStyle1, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3S)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopUnidad)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, detailStyle1, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, detailStyle1, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS)
                .setFooterLabel(glabelUn)
                .setFooterVariablesHeight(12)
                .build();

        if (!tab) {
            System.out.printf("RRERERERRE");
            drb.addField("bodyConceptos", ArrayList.class.getName());
            DynamicReport drFooterSubreport = createFooterSubreport(2);
            drb.addSubreportInGroupFooter(1, drFooterSubreport, new ClassicLayoutManager(),
                    "bodyConceptos", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

            //   DynamicReport drFooterSubreport3 = createResumenCostos(2);
            //   drb.addConcatenatedReport(drFooterSubreport3, new ClassicLayoutManager(), "resumen", DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);


        } else {
            drb.addGlobalFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null);
        }


        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(6, 3, "C. Unitarios");
            drb.setColspan(9, 4, "C. Total");


        } else if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(8, 3, "C. Unitarios");
            drb.setColspan(11, 4, "C. Total");

        } else if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);

            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(9, 3, "C. Unitarios");
            drb.setColspan(12, 4, "C. Total");


        } else if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);

            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(10, 3, "C. Unitarios");
            drb.setColspan(13, 4, "C. Total");

        } else if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);

            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(11, 3, "C. Unitarios");
            drb.setColspan(14, 4, "C. Total");

        } else if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(12, 3, "C. Unitarios");
            drb.setColspan(15, 4, "C. Total");

        } else if (niveles == 15) {
            drb.addGroup(g1);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(8, 3, "C. Unitarios");
            drb.setColspan(11, 4, "C. Total");

        } else if (niveles == 125) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(9, 3, "C. Unitarios");
            drb.setColspan(12, 4, "C. Total");

        } else if (niveles == 1235) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(10, 3, "C. Unitarios");
            drb.setColspan(13, 4, "C. Total");

        } else if (niveles == 12345) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(11, 3, "C. Unitarios");
            drb.setColspan(14, 4, "C. Total");

        } else if (niveles == 156) {
            drb.addGroup(g1);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(9, 3, "C. Unitarios");
            drb.setColspan(12, 4, "C. Total");

        } else if (niveles == 1256) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(10, 3, "C. Unitarios");
            drb.setColspan(13, 4, "C. Total");
        } else if (niveles == 12356) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(11, 3, "C. Unitarios");
            drb.setColspan(14, 4, "C. Total");

        } else if (niveles == 123456) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamat);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(12, 3, "C. Unitarios");
            drb.setColspan(15, 4, "C. Total");
        }


        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;
    }

    public DynamicReport createControlPresupReportUORVToRV(ReportesController reportes, int niveles, boolean flag, int temp, boolean tab) throws ClassNotFoundException {

        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style detailStyle1 = new Style();
        detailStyle1.setVerticalAlign(VerticalAlign.TOP);
        detailStyle1.setHorizontalAlign(HorizontalAlign.CENTER);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerStyle2 = new Style();
        headerStyle2.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle2.setBorderBottom(Border.PEN_1_POINT());
        headerStyle2.setBorderTop(Border.PEN_1_POINT());
        headerStyle2.setBackgroundColor(Color.white);
        headerStyle2.setTextColor(Color.DARK_GRAY);
        headerStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle2.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle2.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        if (temp == 1) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setGrandTotalLegend("Costos Directos Obra")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LandScape.jrxml");
        } else if (temp == 2) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setGrandTotalLegend("Costos Directos Obra")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LetterPie.jrxml");
        }

        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopUnidad = ColumnBuilder.getNew()
                .setColumnProperty("unidad", String.class.getName())
                .setTitle(" ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopid = ColumnBuilder.getNew()
                .setColumnProperty("id", Integer.class.getName())
                .setTitle("Id: ").setWidth(8)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Código").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle2)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(45)
                .setStyle(detailStyle).setHeaderStyle(headerStyle2)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamaterial = ColumnBuilder.getNew()
                .setColumnProperty("material", Double.class.getName())
                .setTitle("Mat.").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamano = ColumnBuilder.getNew()
                .setColumnProperty("mano", Double.class.getName())
                .setTitle("Mano").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaequip = ColumnBuilder.getNew()
                .setColumnProperty("equip", Double.class.getName())
                .setTitle("Equip").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacostMat = ColumnBuilder.getNew()
                .setColumnProperty("costMat", Double.class.getName())
                .setTitle("C. Mater").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostMano = ColumnBuilder.getNew()
                .setColumnProperty("costMano", Double.class.getName())
                .setTitle("C. Mano").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostEqup = ColumnBuilder.getNew()
                .setColumnProperty("costEquip", Double.class.getName())
                .setTitle("C. Equip").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaTotal = ColumnBuilder.getNew()
                .setColumnProperty("total", Double.class.getName())
                .setTitle("Costo Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gb8 = new GroupBuilder();

        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Material", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Mano Obra", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Equipos", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Costo Total", glabelStyle);


        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Especialidad: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Subespecialidad: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabelUni = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("unidad");
            }
        }, glabelStyle2, null);


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Z)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3O)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3N)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Es)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3S)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopUnidad)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabelUni)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) columngruopid)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();


        if (!tab) {
            drb.addField("bodyConceptos", ArrayList.class.getName());
            DynamicReport drFooterSubreport = createFooterSubreport(2);
            drb.addSubreportInGroupFooter(1, drFooterSubreport, new ClassicLayoutManager(),
                    "bodyConceptos", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

        } else {
            drb.addGlobalFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null);
        }

        if (niveles == 1 && flag == false) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columngruopid);

            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(8, 3, "C. Unitarios");
            drb.setColspan(11, 4, "C. Totales");

        } else if (niveles == 2 && flag == false) {

            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addColumn(columngruopid);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(9, 3, "C. Unitarios");
            drb.setColspan(12, 4, "C. Totales");
        } else if (niveles == 3 && flag == false) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);

            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addColumn(columngruopid);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(10, 3, "C. Unitarios");
            drb.setColspan(13, 4, "C. Totales");
        } else if (niveles == 4 && flag == false) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);

            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addColumn(columngruopid);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(11, 3, "C. Unitarios");
            drb.setColspan(14, 4, "C. Totales");
        } else if (niveles == 5 && flag == false) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);

            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addColumn(columngruopid);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(12, 3, "C. Unitarios");
            drb.setColspan(15, 4, "C. Totales");
        } else if (niveles == 6 && flag == false) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addColumn(columngruopid);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(13, 3, "C. Unitarios");
            drb.setColspan(16, 4, "C. Totales");
        } else if (niveles == 9 && flag == true) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g4);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addColumn(columngruopid);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(13, 3, "C. Unitarios");
            drb.setColspan(16, 4, "C. Totales");
        } else if (niveles == 15) {
            drb.addGroup(g1);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(8, 3, "C. Unitarios");
            drb.setColspan(11, 4, "C. Total");

        } else if (niveles == 125) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(9, 3, "C. Unitarios");
            drb.setColspan(12, 4, "C. Total");

        } else if (niveles == 1235) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(10, 3, "C. Unitarios");
            drb.setColspan(13, 4, "C. Total");

        } else if (niveles == 12345) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(11, 3, "C. Unitarios");
            drb.setColspan(14, 4, "C. Total");

        } else if (niveles == 156) {
            drb.addGroup(g1);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(9, 3, "C. Unitarios");
            drb.setColspan(12, 4, "C. Total");

        } else if (niveles == 1256) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(10, 3, "C. Unitarios");
            drb.setColspan(13, 4, "C. Total");
        } else if (niveles == 12356) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequip);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(11, 3, "C. Unitarios");
            drb.setColspan(14, 4, "C. Total");

        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();


        return dr;

    }


    public DynamicReport createControlPresupReportUORVToRVExtend(ReportesController reportes, int niveles, boolean flag, int temp, boolean tab, boolean ext) throws ClassNotFoundException {

        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style detailStyle1 = new Style();
        detailStyle1.setVerticalAlign(VerticalAlign.TOP);
        detailStyle1.setHorizontalAlign(HorizontalAlign.CENTER);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerStyle2 = new Style();
        headerStyle2.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle2.setBorderBottom(Border.PEN_1_POINT());
        headerStyle2.setBorderTop(Border.PEN_1_POINT());
        headerStyle2.setBackgroundColor(Color.white);
        headerStyle2.setTextColor(Color.DARK_GRAY);
        headerStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle2.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle2.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        if (temp == 1) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setGrandTotalLegend("Costos Directos Obra")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LandScape.jrxml");
        } else if (temp == 2) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setGrandTotalLegend("Costos Directos Obra")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LetterPie.jrxml");
        }

        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopUnidad = ColumnBuilder.getNew()
                .setColumnProperty("unidad", String.class.getName())
                .setTitle(" ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopid = ColumnBuilder.getNew()
                .setColumnProperty("id", Integer.class.getName())
                .setTitle("Id: ").setWidth(8)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Código").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle2)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(45)
                .setStyle(detailStyle).setHeaderStyle(headerStyle2)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamaterial = ColumnBuilder.getNew()
                .setColumnProperty("material", Double.class.getName())
                .setTitle("Mat.").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamano = ColumnBuilder.getNew()
                .setColumnProperty("mano", Double.class.getName())
                .setTitle("Mano").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaequip = ColumnBuilder.getNew()
                .setColumnProperty("equip", Double.class.getName())
                .setTitle("Equip").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacostMat = ColumnBuilder.getNew()
                .setColumnProperty("costMat", Double.class.getName())
                .setTitle("C. Mater").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostMano = ColumnBuilder.getNew()
                .setColumnProperty("costMano", Double.class.getName())
                .setTitle("C. Mano").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostEqup = ColumnBuilder.getNew()
                .setColumnProperty("costEquip", Double.class.getName())
                .setTitle("C. Equip").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaTotal = ColumnBuilder.getNew()
                .setColumnProperty("total", Double.class.getName())
                .setTitle("Costo Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columngruopSob = ColumnBuilder.getNew()
                .setColumnProperty("sobreGrup", String.class.getName())
                .setTitle("SobreGrupo: ").setWidth(68)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopGru = ColumnBuilder.getNew()
                .setColumnProperty("rvGrup", String.class.getName())
                .setTitle("Grupo: ").setWidth(38)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopSub = ColumnBuilder.getNew()
                .setColumnProperty("getSubreGrup", String.class.getName())
                .setTitle("SubGrupo: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gb8 = new GroupBuilder();
        GroupBuilder gb9 = new GroupBuilder();
        GroupBuilder gb10 = new GroupBuilder();
        GroupBuilder gb11 = new GroupBuilder();

        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Material", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Mano Obra", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Equipos", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Costo Total", glabelStyle);


        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Especialidad: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Subespecialidad: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabelUni = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("unidad");
            }
        }, glabelStyle2, null);


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Z)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3O)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3N)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Es)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3S)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopUnidad)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabelUni)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) columngruopSob)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g9 = gb9.setCriteriaColumn((PropertyColumn) columngruopGru)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g10 = gb10.setCriteriaColumn((PropertyColumn) columngruopSub)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g11 = gb11.setCriteriaColumn((PropertyColumn) columngruopid)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();


        if (!tab) {
            drb.addField("bodyConceptos", ArrayList.class.getName());
            DynamicReport drFooterSubreport = createFooterSubreport(2);
            drb.addSubreportInGroupFooter(1, drFooterSubreport, new ClassicLayoutManager(),
                    "bodyConceptos", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

        } else {
            drb.addGlobalFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null);
        }

        if (niveles == 1 && flag == false) {


            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g7);

                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopUnidad);

                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);

                drb.addGroup(g11);
                drb.addColumn(columngruopid);

                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(11, 3, "C. Unitarios");
                drb.setColspan(14, 4, "C. Totales");

            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g7);

                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopUnidad);

                drb.addGroup(g11);
                drb.addColumn(columngruopid);

                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(8, 3, "C. Unitarios");
                drb.setColspan(11, 4, "C. Totales");
            }


        } else if (niveles == 2 && flag == false) {

            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g7);

                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);

                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(12, 3, "C. Unitarios");
                drb.setColspan(15, 4, "C. Totales");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g7);

                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(9, 3, "C. Unitarios");
                drb.setColspan(12, 4, "C. Totales");
            }


        } else if (niveles == 3 && flag == false) {

            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g7);


                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);

                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(13, 3, "C. Unitarios");
                drb.setColspan(16, 4, "C. Totales");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);

                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);

                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);

                drb.setColspan(10, 3, "C. Unitarios");
                drb.setColspan(13, 4, "C. Totales");
            }

        } else if (niveles == 4 && flag == false) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g4);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(14, 3, "C. Unitarios");
                drb.setColspan(17, 4, "C. Totales");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g4);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(11, 3, "C. Unitarios");
                drb.setColspan(14, 4, "C. Totales");
            }

        } else if (niveles == 5 && flag == false) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g4);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(15, 3, "C. Unitarios");
                drb.setColspan(18, 4, "C. Totales");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g4);
                drb.addGroup(g5);

                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(12, 3, "C. Unitarios");
                drb.setColspan(15, 4, "C. Totales");
            }

        } else if (niveles == 6 && flag == false) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g4);
                drb.addGroup(g5);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(16, 3, "C. Unitarios");
                drb.setColspan(19, 4, "C. Totales");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g4);
                drb.addGroup(g5);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(13, 3, "C. Unitarios");
                drb.setColspan(16, 4, "C. Totales");
            }

        } else if (niveles == 9 && flag == true) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g5);
                drb.addGroup(g4);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(16, 3, "C. Unitarios");
                drb.setColspan(19, 4, "C. Totales");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g5);
                drb.addGroup(g4);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(13, 3, "C. Unitarios");
                drb.setColspan(16, 4, "C. Totales");
            }

        } else if (niveles == 15) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(12, 3, "C. Unitarios");
                drb.setColspan(15, 4, "C. Total");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(9, 3, "C. Unitarios");
                drb.setColspan(12, 4, "C. Total");
            }
        } else if (niveles == 125) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(13, 3, "C. Unitarios");
                drb.setColspan(16, 4, "C. Total");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);

                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(10, 3, "C. Unitarios");
                drb.setColspan(13, 4, "C. Total");
            }


        } else if (niveles == 1235) {

            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(14, 3, "C. Unitarios");
                drb.setColspan(17, 4, "C. Total");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(11, 3, "C. Unitarios");
                drb.setColspan(14, 4, "C. Total");
            }
        } else if (niveles == 12345) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g4);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(15, 3, "C. Unitarios");
                drb.setColspan(18, 4, "C. Total");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g4);
                drb.addGroup(g5);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruopNiv);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(12, 3, "C. Unitarios");
                drb.setColspan(15, 4, "C. Total");
            }
        } else if (niveles == 156) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g5);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(12, 3, "C. Unitarios");
                drb.setColspan(15, 4, "C. Total");

            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g5);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(9, 3, "C. Unitarios");
                drb.setColspan(12, 4, "C. Total");

            }

        } else if (niveles == 1256) {
            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g5);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(13, 3, "C. Unitarios");
                drb.setColspan(16, 4, "C. Total");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g5);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(10, 3, "C. Unitarios");
                drb.setColspan(13, 4, "C. Total");
            }

        } else if (niveles == 12356) {

            if (ext == true) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g5);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g8);
                drb.addGroup(g9);
                drb.addGroup(g10);
                drb.addGroup(g11);
                drb.addColumn(columngruopSob);
                drb.addColumn(columngruopGru);
                drb.addColumn(columngruopSub);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(14, 3, "C. Unitarios");
                drb.setColspan(17, 4, "C. Total");
            } else if (ext == false) {
                drb.addGroup(g1);
                drb.addGroup(g2);
                drb.addGroup(g3);
                drb.addGroup(g5);
                drb.addGroup(g6);
                drb.addGroup(g7);
                drb.addGroup(g11);
                drb.addColumn(columngruopid);
                drb.addColumn(columngruopEmpresa);
                drb.addColumn(columngruopZona);
                drb.addColumn(columngruopObj);
                drb.addColumn(columngruoesp);
                drb.addColumn(columngruopsup);
                drb.addColumn(columngruopUnidad);
                drb.addColumn(columnauo);
                drb.addColumn(columnadescrip);
                drb.addColumn(columnaum);
                drb.addColumn(columnaCantidad);
                drb.addColumn(columnamaterial);
                drb.addColumn(columnamano);
                drb.addColumn(columnaequip);
                drb.addColumn(columnacostMat);
                drb.addColumn(columnaCostMano);
                drb.addColumn(columnaCostEqup);
                drb.addColumn(columnaTotal);
                drb.setColspan(11, 3, "C. Unitarios");
                drb.setColspan(14, 4, "C. Total");
            }

        }
        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }


    private DynamicReport createFooterSubreport(int temp) {
        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();

        if (temp == 1) {
            Integer margin = (10);
            drb
                    .setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setTemplateFile("templete/reportLetterConcepts.jrxml")
                    .setTitle("Conceptos de Gastos")
                    .setUseFullPageWidth(true)
                    .setOddRowBackgroundStyle(oddRowStyle);
        } else if (temp == 2) {
            Integer margin = (10);
            drb
                    .setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setTemplateFile("templete/reportA4.jrxml")
                    .setTitle("Conceptos de Gastos")
                    .setUseFullPageWidth(true)
                    .setOddRowBackgroundStyle(oddRowStyle);
        }

        AbstractColumn indice = ColumnBuilder.getNew()
                .setColumnProperty("indice", String.class.getName())
                .setTitle("No").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn materiales = ColumnBuilder.getNew()
                .setColumnProperty("concepto", String.class.getName())
                .setTitle("Conceptos").setWidth(55)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn formula = ColumnBuilder.getNew()
                .setColumnProperty("formula", String.class.getName())
                .setTitle("Fórmula").setWidth(25)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn mano = ColumnBuilder.getNew()
                .setColumnProperty("coeficiente", Double.class.getName())
                .setTitle("Coef").setWidth(10)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn equipo = ColumnBuilder.getNew()
                .setColumnProperty("valor", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(indice);
        drb.addColumn(materiales);
        drb.addColumn(formula);
        drb.addColumn(mano);
        drb.addColumn(equipo);


        DynamicReport dr = drb.build();


        return dr;
    }


    public DynamicReport createTotalReport(ReportesController reportes, int niveles) throws ClassNotFoundException {

        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8Letter.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("bodyConceptos", ArrayList.class.getName());
        DynamicReport drFooterSubreport = createFooterSubreport(1);
        drb.addSubreportInGroupFooter(1, drFooterSubreport, new ClassicLayoutManager(),
                "bodyConceptos", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

        // DynamicReport drFooterSubreport3 = createResumenCostos(1);
        // drb.addConcatenatedReport(drFooterSubreport3, new ClassicLayoutManager(), "resumen", DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacostMat = ColumnBuilder.getNew()
                .setColumnProperty("materiales", Double.class.getName())
                .setTitle("Costo Material").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostMano = ColumnBuilder.getNew()
                .setColumnProperty("mano", Double.class.getName())
                .setTitle("Costo Mano Obra").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostEqup = ColumnBuilder.getNew()
                .setColumnProperty("equipos", Double.class.getName())
                .setTitle("Costo Equipo").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaTotal = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();

        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setTextColor(Color.DARK_GRAY)
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Total Material", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Total Mano Obra", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Total Equipos", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Costo Total", glabelStyle);

        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addColumn(columngruopEmpresa);

            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

        } else if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

        } else if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);


            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

        } else if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);


            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);


        } else if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);


            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

        } else if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);


            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

        } else if (niveles == 15) {
            drb.addGroup(g1);
            drb.addGroup(g5);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

        }


        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }

    public DynamicReport createResumenCostos(int temp) {

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();

        if (temp == 1) {
            Integer margin = (10);
            drb
                    .setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setTemplateFile("templete/LetterblankTemplate.jrxml")
                    .setTitle("Total por Conceptos de Gastos")
                    .setUseFullPageWidth(true)
                    .setOddRowBackgroundStyle(oddRowStyle);
        } else if (temp == 2) {
            Integer margin = (10);
            drb
                    .setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setTemplateFile("templete/blankTemplate.jrxml")
                    .setTitle("Total por Conceptos de Gastos")
                    .setUseFullPageWidth(true)
                    .setOddRowBackgroundStyle(oddRowStyle);
        }


        AbstractColumn columnaCustomExpression = ColumnBuilder.getNew()
                .setCustomExpression(new RecordsInPageCustomExpression())
                .setTitle("No.").setWidth(4)
                .setStyle(detailStyle).setHeaderStyle(headerStyle).build();


        AbstractColumn materiales = ColumnBuilder.getNew()
                .setColumnProperty("conceptos", String.class.getName())
                .setTitle("Conceptos").setWidth(55)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn equipo = ColumnBuilder.getNew()
                .setColumnProperty("valor", String.class.getName())
                .setTitle("Valor").setWidth(12)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(columnaCustomExpression);
        drb.addColumn(materiales);
        drb.addColumn(equipo);


        DynamicReport dr = drb.build();
        return dr;
    }


    public DynamicReport createResumenCostosByResumen() {

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();

        Integer margin = (10);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setTemplateFile("templete/report8Letter.jrxml")
                .setUseFullPageWidth(true)
                .setOddRowBackgroundStyle(oddRowStyle);

        AbstractColumn indice = ColumnBuilder.getNew()
                .setColumnProperty("indice", String.class.getName())
                .setTitle("No.").setWidth(5)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn materiales = ColumnBuilder.getNew()
                .setColumnProperty("conceptos", String.class.getName())
                .setTitle("Conceptos").setWidth(55)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn equipo = ColumnBuilder.getNew()
                .setColumnProperty("valor", String.class.getName())
                .setTitle("Valor").setWidth(12)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(indice);
        drb.addColumn(materiales);
        drb.addColumn(equipo);


        DynamicReport dr = drb.build();
        return dr;
    }


    public DynamicReport createTotalReportToRVCetToPlan(ReportesController reportes, int niveles) throws ClassNotFoundException {

        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/reportTotalCert.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacostMat = ColumnBuilder.getNew()
                .setColumnProperty("costMat", Double.class.getName())
                .setTitle("Costo Material").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostMano = ColumnBuilder.getNew()
                .setColumnProperty("costMano", Double.class.getName())
                .setTitle("Costo Mano Obra").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostEqup = ColumnBuilder.getNew()
                .setColumnProperty("costEquip", Double.class.getName())
                .setTitle("Costo Equipo").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostSalario = ColumnBuilder.getNew()
                .setColumnProperty("costSalario", Double.class.getName())
                .setTitle("Salario").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaTotal = ColumnBuilder.getNew()
                .setColumnProperty("total", Double.class.getName())
                .setTitle("Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();


        if (niveles == 1) {
            drb.addGroup(g1);

            drb.addColumn(columngruopEmpresa);

            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaCostSalario);
            drb.addColumn(columnaTotal);

        }

        if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaCostSalario);
            drb.addColumn(columnaTotal);
        }

        if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);


            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaCostSalario);
            drb.addColumn(columnaTotal);
        }

        if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);


            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaCostSalario);
            drb.addColumn(columnaTotal);
        }

        if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);


            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaCostSalario);
            drb.addColumn(columnaTotal);
        }

        if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);


            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaCostSalario);
            drb.addColumn(columnaTotal);
        }


        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }


    public DynamicReport createCertificacionOrigen(ReportesController reportes, int niveles, int temp, boolean tab) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        if (temp == 1) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setGrandTotalLegend("Sub Total Costos de la Construcción de la Obra: ")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LetterCertif.jrxml");
        } else if (temp == 2) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setGrandTotalLegend("Sub Total Costos de la Construcción de la Obra: ")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LetterPieCertif.jrxml");
        }

        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);


/**
 * grupos en el reporte
 */

        AbstractColumn empresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn zona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn objeto = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn nivel = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn especialidad = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn subespecialidad = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn brigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada: ").setWidth(45)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn grupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn cuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla: ").setWidth(45)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        /**
         * Contenido de la table
         */

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("unidad", String.class.getName())
                .setTitle("Código").setWidth(10)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("descripUnidad", String.class.getName())
                .setTitle("Descripción").setWidth(40)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("uoUM", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn materiales = ColumnBuilder.getNew()
                .setColumnProperty("costMat", Double.class.getName())
                .setTitle("C. Mater").setWidth(13).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
        AbstractColumn mano = ColumnBuilder.getNew()
                .setColumnProperty("costMano", Double.class.getName())
                .setTitle("C. Mano").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
        AbstractColumn equipo = ColumnBuilder.getNew()
                .setColumnProperty("costEquip", Double.class.getName())
                .setTitle("C. Equip").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatotal = ColumnBuilder.getNew()
                .setColumnProperty("total", Double.class.getName())
                .setTitle("Costo Total").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        drb.addColumn(columnacode);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        drb.addColumn(columnacant);
        drb.addColumn(materiales);
        drb.addColumn(mano);
        drb.addColumn(equipo);
        drb.addColumn(columnatotal);


        DJGroupLabel glabel1 = new DJGroupLabel("Material", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Mano Obra", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Equipos", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Costo Total", glabelStyle);

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.BOTTOM)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Br = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("brigada");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Gr = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("grupo");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Cu = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("cuadrilla");
            }
        }, glabelStyle2, null);

        if (!tab) {
            drb.addField("bodyConceptos", ArrayList.class.getName());
            DynamicReport drFooterSubreport = createFooterSubreport(temp);
            drb.addSubreportInGroupFooter(1, drFooterSubreport, new ClassicLayoutManager(),
                    "bodyConceptos", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

        } else {
            drb.addGlobalFooterVariable(materiales, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(mano, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(equipo, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null);
        }

        //  DynamicReport drFooterSubreport3 = createResumenCostos(1);
        // drb.addConcatenatedReport(drFooterSubreport3, new ClassicLayoutManager(), "resumen", DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);


        if (niveles <= 6) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);
            drb.addColumn(especialidad);
            drb.addColumn(subespecialidad);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) subespecialidad)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g6);

        } else if (niveles == 7) {
            drb.addColumn(empresa);
            drb.addColumn(brigada);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);
            drb.addColumn(especialidad);
            drb.addColumn(subespecialidad);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb7 = new GroupBuilder();
            DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) brigada)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Br)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g7);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) subespecialidad)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g6);


        } else if (niveles == 8) {
            drb.addColumn(empresa);
            drb.addColumn(brigada);
            drb.addColumn(grupo);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);
            drb.addColumn(especialidad);
            drb.addColumn(subespecialidad);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb7 = new GroupBuilder();
            DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) brigada)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Br)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g7);

            GroupBuilder gb8 = new GroupBuilder();
            DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) grupo)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Gr)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g8);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) subespecialidad)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g6);

        } else if (niveles == 9) {
            drb.addColumn(empresa);
            drb.addColumn(brigada);
            drb.addColumn(grupo);
            drb.addColumn(cuadrilla);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);
            drb.addColumn(especialidad);
            drb.addColumn(subespecialidad);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb7 = new GroupBuilder();
            DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) brigada)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Br)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g7);

            GroupBuilder gb8 = new GroupBuilder();
            DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) grupo)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Gr)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g8);


            GroupBuilder gb9 = new GroupBuilder();
            DJGroup g9 = gb9.setCriteriaColumn((PropertyColumn) cuadrilla)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Cu)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g9);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) subespecialidad)
                    .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(mano, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(equipo, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g6);
        }

        //   drb.addGlobalFooterVariable(columnatotal, DJCalculation.SUM, headerVariables, null);

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }


    public DynamicReport createReportAcomulado(ReportesController reportes, int niveles) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/reportCertificacionOrigen.jrxml");


/**
 * grupos en el reporte
 */

        AbstractColumn empresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn zona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn objeto = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn nivel = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn especialidad = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn subespecialidad = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        /**
         * Contenido de la table
         */

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigoUO", String.class.getName())
                .setTitle("Código").setWidth(12)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("descripcionUO", String.class.getName())
                .setTitle("Descripción").setWidth(40)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("umUO", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cant", Double.class.getName())
                .setTitle("Cant.").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn volCert = ColumnBuilder.getNew()
                .setColumnProperty("cantCert", Double.class.getName())
                .setTitle("Vol. Certf").setWidth(13).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn valor = ColumnBuilder.getNew()
                .setColumnProperty("monto", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn ejecutado = ColumnBuilder.getNew()
                .setColumnProperty("montocertf", Double.class.getName())
                .setTitle("Ejecutado").setWidth(13).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn pentV = ColumnBuilder.getNew()
                .setColumnProperty("cantPend", Double.class.getName())
                .setTitle("Pend(Vol)").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn pentMon = ColumnBuilder.getNew()
                .setColumnProperty("montoPend", Double.class.getName())
                .setTitle("Pend(Valor)").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(columnacode);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        drb.addColumn(columnacant);
        drb.addColumn(volCert);
        drb.addColumn(valor);
        drb.addColumn(ejecutado);
        drb.addColumn(pentV);
        drb.addColumn(pentMon);


        if (niveles == 1) {
            GroupBuilder gb1 = new GroupBuilder();

            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(ejecutado, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(pentMon, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(empresa);
            drb.addGroup(g1);


        } else if (niveles == 2) {
            drb.addColumn(empresa);
            drb.addColumn(zona);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(ejecutado, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(pentMon, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g2);


        } else if (niveles == 3) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);


            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(ejecutado, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(pentMon, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g3);


        } else if (niveles == 4) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(ejecutado, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(pentMon, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g4);

        } else if (niveles == 5) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);
            drb.addColumn(especialidad);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(ejecutado, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(pentMon, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g5);

        } else if (niveles == 6) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);
            drb.addColumn(especialidad);
            drb.addColumn(subespecialidad);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g4);
            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g5);
            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) subespecialidad)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(ejecutado, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(pentMon, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g6);
        }
        drb.setUseFullPageWidth(true);

        DynamicReport dr = drb.build();


        return dr;

    }

    public DynamicReport createReportPendientes(ReportesController reportes, int niveles) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setGrandTotalLegend("Total Pendiente por Ejecutar")
                .setTemplateFile("templete/report8LandScape.jrxml");


/**
 * grupos en el reporte
 */

        AbstractColumn empresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn zona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn objeto = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn nivel = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn especialidad = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn subespecialidad = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        /**
         * Contenido de la table
         */

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigoUO", String.class.getName())
                .setTitle("Código").setWidth(12)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("descripcionUO", String.class.getName())
                .setTitle("Descripción").setWidth(40)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("umUO", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacantPres = ColumnBuilder.getNew()
                .setColumnProperty("cantPre", Double.class.getName())
                .setTitle("Cant. Presup").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cant", Double.class.getName())
                .setTitle("Cant. Pend").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn volCert = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(13).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn valor = ColumnBuilder.getNew()
                .setColumnProperty("monto", Double.class.getName())
                .setTitle("Valor Pend").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn tipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle(" ")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(columnacode);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        drb.addColumn(columnacantPres);
        drb.addColumn(columnacant);
        drb.addColumn(volCert);
        drb.addColumn(valor);


        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.BOTTOM)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Especialidad: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total SubEspecialidad: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabelTipo = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Tipo de Insumo: ";
            }
        }, glabelStyle2, null);

        drb.addGlobalFooterVariable(valor, DJCalculation.SUM, headerVariables, null);


        if (niveles == 1) {
            drb.addColumn(tipo);
            GroupBuilder gbtotal = new GroupBuilder();
            DJGroup total = gbtotal.setCriteriaColumn((PropertyColumn) tipo)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabelTipo)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(total);


        } else if (niveles == 2) {
            drb.addColumn(empresa);
            drb.addColumn(zona);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g2);

            GroupBuilder gbtotal = new GroupBuilder();
            DJGroup total = gbtotal.setCriteriaColumn((PropertyColumn) tipo)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(tipo);
            drb.addGroup(total);


        } else if (niveles == 3) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g2);


            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gbtotal = new GroupBuilder();
            DJGroup total = gbtotal.setCriteriaColumn((PropertyColumn) tipo)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(tipo);
            drb.addGroup(total);


        } else if (niveles == 4) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gbtotal = new GroupBuilder();
            DJGroup total = gbtotal.setCriteriaColumn((PropertyColumn) tipo)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(tipo);
            drb.addGroup(total);

        } else if (niveles == 5) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);
            drb.addColumn(especialidad);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gbtotal = new GroupBuilder();
            DJGroup total = gbtotal.setCriteriaColumn((PropertyColumn) tipo)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(tipo);
            drb.addGroup(total);

        } else if (niveles == 6) {
            drb.addColumn(empresa);
            drb.addColumn(zona);
            drb.addColumn(objeto);
            drb.addColumn(nivel);
            drb.addColumn(especialidad);
            drb.addColumn(subespecialidad);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) zona)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) objeto)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) nivel)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g4);
            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) subespecialidad)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g6);

            GroupBuilder gbtotal = new GroupBuilder();
            DJGroup total = gbtotal.setCriteriaColumn((PropertyColumn) tipo)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(tipo);
            drb.addGroup(total);
        } else if (niveles == 15) {
            drb.addColumn(especialidad);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) especialidad)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);


            GroupBuilder gbtotal = new GroupBuilder();
            DJGroup total = gbtotal.setCriteriaColumn((PropertyColumn) tipo)
                    .addFooterVariable(valor, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(tipo);
            drb.addGroup(total);
        }
        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;
    }


    public DynamicReport createCertificacionOrigenUORV(ReportesController reportes, int niveles, int temp, boolean tab) throws ClassNotFoundException {

        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style detail2Style = new Style();
        detail2Style.setVerticalAlign(VerticalAlign.TOP);
        detail2Style.setHorizontalAlign(HorizontalAlign.LEFT);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        if (temp == 1) {
            Integer margin = new Integer(20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setGrandTotalLegend("Sub Total Costos de la Construcción de la Obra: ")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LandScapeCertif.jrxml");
        } else if (temp == 2) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    .setGrandTotalLegend("Sub Total Costos de la Construcción de la Obra: ")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LetterPieCertif.jrxml");
        }

        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn brigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn grupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn cuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopUnidad = ColumnBuilder.getNew()
                .setColumnProperty("unidad", String.class.getName())
                .setTitle(" ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("codeRV", String.class.getName())
                .setTitle("Código").setWidth(17)
                .setStyle(detail2Style).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descRV", String.class.getName())
                .setTitle("Descripción").setWidth(45)
                .setStyle(detail2Style).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("umRV", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamaterial = ColumnBuilder.getNew()
                .setColumnProperty("costMat", Double.class.getName())
                .setTitle("Mat").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamano = ColumnBuilder.getNew()
                .setColumnProperty("costMano", Double.class.getName())
                .setTitle("Mano").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaequipo = ColumnBuilder.getNew()
                .setColumnProperty("costEquip", Double.class.getName())
                .setTitle("Equip.").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacostMat = ColumnBuilder.getNew()
                .setColumnProperty("costMatRV", Double.class.getName())
                .setTitle("C. Mater").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostMano = ColumnBuilder.getNew()
                .setColumnProperty("costManoRV", Double.class.getName())
                .setTitle("C. Mano").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostEqup = ColumnBuilder.getNew()
                .setColumnProperty("costEquipoRV", Double.class.getName())
                .setTitle("C. Equip.").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaTotal = ColumnBuilder.getNew()
                .setColumnProperty("costTotalCertRV", Double.class.getName())
                .setTitle("Costo Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gb8 = new GroupBuilder();
        GroupBuilder gb9 = new GroupBuilder();
        GroupBuilder gb10 = new GroupBuilder();

        //DJGroupLabel glabel1 = new DJGroupLabel("Material", glabelStyle);
        //DJGroupLabel glabel2 = new DJGroupLabel("Mano Obra", glabelStyle);
        //DJGroupLabel glabel3 = new DJGroupLabel("Equipos", glabelStyle);
        //DJGroupLabel glabel4 = new DJGroupLabel("Costo Total", glabelStyle);

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT)
                .setVerticalAlign(VerticalAlign.BOTTOM)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Br = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("brigada");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Gr = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("grupo");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Cu = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("cuadrilla");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Uni = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Unidad de Obra ";
            }
        }, glabelStyle2, null);

        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Z)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3O)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3N)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Es)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3S)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) brigada)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Br)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) grupo)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Gr)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g9 = gb9.setCriteriaColumn((PropertyColumn) cuadrilla)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Cu)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g10 = gb10.setCriteriaColumn((PropertyColumn) columngruopUnidad)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, null)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS)
                .setFooterLabel(glabel3Uni)
                .setFooterVariablesHeight(12)
                .build();

        if (!tab) {
            drb.addField("bodyConceptos", ArrayList.class.getName());
            DynamicReport drFooterSubreport = createFooterSubreport(2);
            drb.addSubreportInGroupFooter(1, drFooterSubreport, new ClassicLayoutManager(),
                    "bodyConceptos", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

            // DynamicReport drFooterSubreport3 = createResumenCostos(2);
            // drb.addConcatenatedReport(drFooterSubreport3, new ClassicLayoutManager(), "resumen", DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);

        } else {
            drb.addGlobalFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null);
        }


        if (niveles <= 6) {

            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g10);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequipo);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(12, 3, "C. Unitarios");
            drb.setColspan(15, 4, "C. Total");


        } else if (niveles == 7) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g10);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(brigada);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequipo);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(13, 3, "C. Unitarios");
            drb.setColspan(16, 4, "C. Total");

        } else if (niveles == 8) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g10);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(brigada);
            drb.addColumn(grupo);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequipo);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(14, 3, "C. Unitarios");
            drb.setColspan(17, 4, "C. Total");

        } else if (niveles == 9) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addGroup(g9);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g10);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(brigada);
            drb.addColumn(grupo);
            drb.addColumn(cuadrilla);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequipo);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(15, 3, "C. Unitarios");
            drb.setColspan(18, 4, "C. Total");
        }
        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }

    public DynamicReport createCertificacionOrigenRVRV(ReportesController reportes, int niveles, int temp, boolean tab) throws ClassNotFoundException {

        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style detail2Style = new Style();
        detail2Style.setVerticalAlign(VerticalAlign.TOP);
        detail2Style.setHorizontalAlign(HorizontalAlign.LEFT);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        if (temp == 1) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    // .setGrandTotalLegend("Sub Total Costos de la Construcción de la Obra: ")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LandScapeCertif.jrxml");
        } else if (temp == 2) {
            Integer margin = (20);
            drb.setTitleStyle(titleStyle)
                    .setDetailHeight(15).setLeftMargin(margin)
                    .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                    .setPrintBackgroundOnOddRows(false)
                    .setGrandTotalLegendStyle(headerVariables)
                    .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                    .setPrintColumnNames(true)
                    // .setGrandTotalLegend("Sub Total Costos de la Construcción de la Obra: ")
                    .setOddRowBackgroundStyle(oddRowStyle)
                    .setTemplateFile("templete/report8LetterPieCertif.jrxml");
        }

        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn brigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn grupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn cuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopUnidad = ColumnBuilder.getNew()
                .setColumnProperty("unidad", String.class.getName())
                .setTitle(" ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("codeRV", String.class.getName())
                .setTitle("Código").setWidth(17)
                .setStyle(detail2Style).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descRV", String.class.getName())
                .setTitle("Descripción").setWidth(45)
                .setStyle(detail2Style).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("umRV", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantRVCert", Double.class.getName())
                .setTitle("Cant.").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamaterial = ColumnBuilder.getNew()
                .setColumnProperty("costMat", Double.class.getName())
                .setTitle("Mat").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnamano = ColumnBuilder.getNew()
                .setColumnProperty("costMano", Double.class.getName())
                .setTitle("Mano").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaequipo = ColumnBuilder.getNew()
                .setColumnProperty("costEquip", Double.class.getName())
                .setTitle("Equip.").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacostMat = ColumnBuilder.getNew()
                .setColumnProperty("costMatRV", Double.class.getName())
                .setTitle("C. Mater").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostMano = ColumnBuilder.getNew()
                .setColumnProperty("costManoRV", Double.class.getName())
                .setTitle("C. Mano").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCostEqup = ColumnBuilder.getNew()
                .setColumnProperty("costEquipoRV", Double.class.getName())
                .setTitle("C. Equip.").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaTotal = ColumnBuilder.getNew()
                .setColumnProperty("costTotalCertRV", Double.class.getName())
                .setTitle("Costo Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gb8 = new GroupBuilder();
        GroupBuilder gb9 = new GroupBuilder();
        GroupBuilder gb10 = new GroupBuilder();

        DJGroupLabel glabel1 = new DJGroupLabel("Material", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Mano Obra", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Equipos", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Costo Total", glabelStyle);

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.BOTTOM)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Br = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("brigada");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Gr = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("grupo");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Cu = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("cuadrilla");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Uni = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel Especifico";
            }
        }, glabelStyle2, null);

        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Z)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3O)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3N)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Es)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3S)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) brigada)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Br)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) grupo)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Gr)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g9 = gb9.setCriteriaColumn((PropertyColumn) cuadrilla)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Cu)
                .setFooterVariablesHeight(12)
                .build();


        DJGroup g10 = gb10.setCriteriaColumn((PropertyColumn) columngruopUnidad)
                .addFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS)
                .setFooterLabel(glabel3Uni)
                .setFooterVariablesHeight(12)
                .build();

        if (!tab) {
            drb.addField("bodyConceptos", ArrayList.class.getName());
            DynamicReport drFooterSubreport = createFooterSubreport(2);
            drb.addSubreportInGroupFooter(1, drFooterSubreport, new ClassicLayoutManager(),
                    "bodyConceptos", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);
        } else {
            drb.addGlobalFooterVariable(columnacostMat, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostMano, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaCostEqup, DJCalculation.SUM, headerVariables, null);
            drb.addGlobalFooterVariable(columnaTotal, DJCalculation.SUM, headerVariables, null);
        }

        if (niveles <= 6) {

            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g10);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequipo);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);
            drb.setColspan(12, 3, "C. Unitarios");
            drb.setColspan(15, 4, "C. Total");


        } else if (niveles == 7) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g10);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(brigada);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequipo);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(13, 3, "C. Unitarios");
            drb.setColspan(16, 4, "C. Total");

        } else if (niveles == 8) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g10);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(brigada);
            drb.addColumn(grupo);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequipo);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(14, 3, "C. Unitarios");
            drb.setColspan(17, 4, "C. Total");

        } else if (niveles == 9) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g8);
            drb.addGroup(g9);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g10);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(brigada);
            drb.addColumn(grupo);
            drb.addColumn(cuadrilla);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnaCantidad);
            drb.addColumn(columnamaterial);
            drb.addColumn(columnamano);
            drb.addColumn(columnaequipo);
            drb.addColumn(columnacostMat);
            drb.addColumn(columnaCostMano);
            drb.addColumn(columnaCostEqup);
            drb.addColumn(columnaTotal);

            drb.setColspan(15, 3, "C. Unitarios");
            drb.setColspan(18, 4, "C. Total");
        }
        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }

    public DynamicReport cuantitativaReport(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/reportCuantitativaCert.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("datosFondoHorarioExplotacionModels", ArrayList.class.getName());
        DynamicReport drFooterSubreportFHE = createFooterFondoHorarioSubreport();
        drb.addConcatenatedReport(drFooterSubreportFHE, new ClassicLayoutManager(), "datosFondoHorarioExplotacionModels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
/*
        AbstractColumn columnapeso = ColumnBuilder.getNew()
                .setColumnProperty("peso", Double.class.getName())
                .setTitle("Peso").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
*/
        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        Style glabelStyle3 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Subtotal " + fields.get("tipo") + ":";
            }
        }, glabelStyle2, null);


        DJGroupLabel glabelTipo = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Costo Directo: ";
            }
        }, glabelStyle3, null);


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)

                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnatipo)
                .addVariable("horas1", columnacantidad, DJCalculation.SUM)
                .addFooterVariable(columnacantidad, new CustomExpression() {
                    @Override
                    public Object evaluate(Map fields, Map variabled, Map map2) {
                        Double var = 0.0;
                        if (fields.get("tipo").toString().trim().equals("Equipos")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Mano de Obra")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Materiales")) {
                            var = null;
                        }
                        return var;
                    }

                    @Override
                    public String getClassName() {
                        return Double.class.getName();
                    }
                }, headerVariables)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();

        if (niveles == 1) {
            // drb.addGroup(g1);
            drb.addGroup(g7);

            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }

            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);


        }

        if (niveles == 2) {

            // drb.addGroup(g1);
            drb.addGroup(g2);


            drb.addGroup(g7);


            // drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 3) {

            //drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);


            drb.addGroup(g7);

            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 4) {

            //  drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);


            drb.addGroup(g7);

            // drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 5) {

            // drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);


            drb.addGroup(g7);

            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 6) {

            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);


            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }


        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }

    public DynamicReport cuantitativaReportGeneral(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setGrandTotalLegendStyle(headerVariables)
                .setGrandTotalLegend("Costo Total de Recursos ")
                .setTemplateFile("templete/report8LetterCuantitativa.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("empresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("datosFondoHorarioExplotacionModels", ArrayList.class.getName());
        DynamicReport drFooterSubreportFHE = createFooterFondoHorarioSubreport();
        drb.addConcatenatedReport(drFooterSubreportFHE, new ClassicLayoutManager(), "datosFondoHorarioExplotacionModels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(18)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        Style glabelStyle3 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabelTipo = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Subtotal " + fields.get("tipo") + ":";
            }
        }, glabelStyle2, null);


        DJGroupLabel glabelZona = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona " + fields.get("zona").toString().trim() + ":";
            }

        }, glabelStyle3, null);


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                //.setFooterLabel(glabelZona)
                //.setFooterVariablesHeight(12)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        //System.out.println(columnatipo);

        DJGroup g7 = new DJGroup();
        g7 = gb7.setCriteriaColumn((PropertyColumn) columnatipo)
                .addVariable("horas1", columnacantidad, DJCalculation.SUM)
                .addFooterVariable(columnacantidad, new CustomExpression() {
                    @Override
                    public Object evaluate(Map fields, Map variabled, Map map2) {
                        Double var = 0.0;
                        if (fields.get("tipo").toString().trim().equals("Equipos")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Mano de Obra")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Materiales")) {
                            var = null;
                        }
                        return var;
                    }

                    @Override
                    public String getClassName() {
                        return Double.class.getName();
                    }
                }, headerVariables)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabelTipo)
                .setFooterVariablesHeight(12)
                .build();


        drb.addGlobalFooterVariable(columnacosto, DJCalculation.SUM, headerVariables);
        //drb.addGlobalFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, getValueFormatter());

        if (niveles == 1) {
            //drb.addGroup(g1);
            drb.addGroup(g7);
            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }

            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);


        } else if (niveles == 2) {

            // drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);


            // drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 3) {

            //drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);


            drb.addGroup(g7);

            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 4) {

            //  drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);


            drb.addGroup(g7);

            // drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 5) {

            // drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);


            drb.addGroup(g7);

            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 6) {

            // drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);


            drb.addGroup(g7);

            // drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 15) {
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 125) {
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 1235) {
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);

        } else if (niveles == 156) {
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }
        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }

    public DynamicReport cuantitativaReportGeneralCertificacion(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setGrandTotalLegendStyle(headerVariables)
                .setGrandTotalLegend("Costo Total de Recursos ")
                .setTemplateFile("templete/report8LetterCuantitativa.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("empresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("datosFondoHorarioExplotacionModels", ArrayList.class.getName());
        DynamicReport drFooterSubreportFHE = createFooterFondoHorarioSubreport();
        drb.addConcatenatedReport(drFooterSubreportFHE, new ClassicLayoutManager(), "datosFondoHorarioExplotacionModels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopbrigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoepgrupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngroupcuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(18)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gb8 = new GroupBuilder();
        GroupBuilder gb9 = new GroupBuilder();
        GroupBuilder gb10 = new GroupBuilder();

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        Style glabelStyle3 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Subtotal " + fields.get("tipo") + ":";
            }
        }, glabelStyle2, null);


        DJGroupLabel glabelTipo = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Costo Directo: ";
            }
        }, glabelStyle3, null);


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                //  .setFooterLabel(glabelZona)
                // .setFooterVariablesHeight(12)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) columngruopbrigada)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g9 = gb9.setCriteriaColumn((PropertyColumn) columngruoepgrupo)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g10 = gb10.setCriteriaColumn((PropertyColumn) columngroupcuadrilla)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        //System.out.println(columnatipo);

        DJGroup g7 = new DJGroup();
        g7 = gb7.setCriteriaColumn((PropertyColumn) columnatipo)
                .addVariable("horas1", columnacantidad, DJCalculation.SUM)
                .addFooterVariable(columnacantidad, new CustomExpression() {
                    @Override
                    public Object evaluate(Map fields, Map variabled, Map map2) {
                        Double var = 0.0;
                        if (fields.get("tipo").toString().trim().equals("Equipos")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Mano de Obra")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Materiales")) {
                            var = null;
                        }
                        return var;
                    }

                    @Override
                    public String getClassName() {
                        return Double.class.getName();
                    }
                }, headerVariables)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabelTipo)
                .setFooterVariablesHeight(12)
                .build();


        drb.addGlobalFooterVariable(columnacosto, DJCalculation.SUM, headerVariables);
        //drb.addGlobalFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, getValueFormatter());

        if (niveles == 1) {
            //drb.addGroup(g1);
            drb.addGroup(g7);
            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }

            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);


        } else if (niveles == 2) {

            // drb.addGroup(g1);
            drb.addGroup(g2);


            drb.addGroup(g7);


            // drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 3) {

            //drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);


            drb.addGroup(g7);

            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 4) {

            //  drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);


            drb.addGroup(g7);

            // drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 5) {

            // drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);


            drb.addGroup(g7);

            //  drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 6) {

            // drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);


            drb.addGroup(g7);

            // drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);


            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 15) {
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 125) {
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 1235) {
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);

        } else if (niveles == 156) {
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);

        } else if (niveles == 7) {
            drb.addGroup(g8);
            drb.addGroup(g7);

            drb.addColumn(columngruopbrigada);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);

        } else if (niveles == 8) {
            drb.addGroup(g8);
            drb.addGroup(g9);
            drb.addGroup(g7);

            drb.addColumn(columngruopbrigada);
            drb.addColumn(columngruoepgrupo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 9) {
            drb.addGroup(g8);
            drb.addGroup(g9);
            drb.addGroup(g10);
            drb.addGroup(g7);

            drb.addColumn(columngruopbrigada);
            drb.addColumn(columngruoepgrupo);
            drb.addColumn(columngroupcuadrilla);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }


        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }


    /**
     * Para la cuantitativa de la planificación y el plan
     */


    public DynamicReport cuantitativaReportPlanyCert(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;
        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setGrandTotalLegend("Costo Total de Recursos")
                .setTemplateFile("templete/reportPresupuestoCuantitativaCert.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("datosFondoHorarioExplotacionModels", ArrayList.class.getName());
        DynamicReport drFooterSubreportFHE = createFooterFondoHorarioSubreport();
        drb.addConcatenatedReport(drFooterSubreportFHE, new ClassicLayoutManager(), "datosFondoHorarioExplotacionModels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        Style glabelStyle3 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Subtotal " + fields.get("tipo") + ":";
            }
        }, glabelStyle2, null);


        DJGroupLabel glabelTipo = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Costo Directo: ";
            }
        }, glabelStyle3, null);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaUO = ColumnBuilder.getNew()
                .setColumnProperty("uo", String.class.getName())
                .setTitle("UO: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnabrigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnagrupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gbgUn = new GroupBuilder();
        GroupBuilder gbbrigada = new GroupBuilder();
        GroupBuilder gbgrupo = new GroupBuilder();
        GroupBuilder gbcuadrilla = new GroupBuilder();


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                .build();

        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnatipo)
                .addVariable("horas1", columnacantidad, DJCalculation.SUM)
                .addFooterVariable(columnacantidad, new CustomExpression() {
                    @Override
                    public Object evaluate(Map fields, Map variabled, Map map2) {
                        Double var = 0.0;
                        if (fields.get("tipo").toString().trim().equals("Equipos")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Mano de Obra")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Materiales")) {
                            var = null;
                        }
                        return var;
                    }

                    @Override
                    public String getClassName() {
                        return Double.class.getName();
                    }
                }, headerVariables)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();
        DJGroup g8 = gbgUn.setCriteriaColumn((PropertyColumn) columnaUO)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabelTipo)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g9 = gbbrigada.setCriteriaColumn((PropertyColumn) columnabrigada)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g10 = gbgrupo.setCriteriaColumn((PropertyColumn) columnagrupo)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g11 = gbcuadrilla.setCriteriaColumn((PropertyColumn) columnacuadrilla)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addGlobalFooterVariable(columnacosto, DJCalculation.SUM, headerVariables);

        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addGroup(g8);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }

            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);


        } else if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g8);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g8);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g8);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g8);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g8);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);

        } else if (niveles == 7) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g9);
            drb.addGroup(g8);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 8) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g9);
            drb.addGroup(g10);
            drb.addGroup(g8);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 9) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g9);
            drb.addGroup(g10);
            drb.addGroup(g11);
            drb.addGroup(g8);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacuadrilla);
            drb.addColumn(columnaUO);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();

        return dr;
    }


    public DynamicReport cuantitativaReportPlanyCertRV(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;
        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setGrandTotalLegend("Costo Total de Recursos")
                .setTemplateFile("templete/reportPresupuestoCuantitativaCert.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("datosFondoHorarioExplotacionModels", ArrayList.class.getName());
        DynamicReport drFooterSubreportFHE = createFooterFondoHorarioSubreport();
        drb.addConcatenatedReport(drFooterSubreportFHE, new ClassicLayoutManager(), "datosFondoHorarioExplotacionModels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);

        /*
        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.BOTTOM)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();
*/
        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaUO = ColumnBuilder.getNew()
                .setColumnProperty("uo", String.class.getName())
                .setTitle("UO: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnabrigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnagrupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gbgUn = new GroupBuilder();
        GroupBuilder gbbrigada = new GroupBuilder();
        GroupBuilder gbgrupo = new GroupBuilder();
        GroupBuilder gbcuadrilla = new GroupBuilder();

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        Style glabelStyle3 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Subtotal " + fields.get("tipo") + ":";
            }
        }, glabelStyle2, null);


        DJGroupLabel glabelTipo = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Costo Directo: ";
            }
        }, glabelStyle3, null);


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                .build();

        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnatipo)
                .addVariable("horas1", columnacantidad, DJCalculation.SUM)
                .addFooterVariable(columnacantidad, new CustomExpression() {
                    @Override
                    public Object evaluate(Map fields, Map variabled, Map map2) {
                        Double var = 0.0;
                        if (fields.get("tipo").toString().trim().equals("Equipos")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Mano de Obra")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Materiales")) {
                            var = null;
                        }
                        return var;
                    }

                    @Override
                    public String getClassName() {
                        return Double.class.getName();
                    }
                }, headerVariables)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g9 = gbbrigada.setCriteriaColumn((PropertyColumn) columnabrigada)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g10 = gbgrupo.setCriteriaColumn((PropertyColumn) columnagrupo)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g11 = gbcuadrilla.setCriteriaColumn((PropertyColumn) columnacuadrilla)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addGlobalFooterVariable(columnacosto, DJCalculation.SUM, headerVariables);

        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }

            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);


        } else if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);

        } else if (niveles == 7) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g9);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 8) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g9);
            drb.addGroup(g10);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 9) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g9);
            drb.addGroup(g10);
            drb.addGroup(g11);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacuadrilla);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();

        return dr;
    }


    /**
     * PLAN y Certificaciones General
     */
    public DynamicReport cuantitativaReportPlanyCertGENRAL(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8Letter.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("datosFondoHorarioExplotacionModels", ArrayList.class.getName());
        DynamicReport drFooterSubreportFHE = createFooterFondoHorarioSubreport();
        drb.addConcatenatedReport(drFooterSubreportFHE, new ClassicLayoutManager(), "datosFondoHorarioExplotacionModels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(55)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnabrigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnagrupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gbgUn = new GroupBuilder();
        GroupBuilder gbbrigada = new GroupBuilder();
        GroupBuilder gbgrupo = new GroupBuilder();
        GroupBuilder gbcuadrilla = new GroupBuilder();


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();


        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnatipo)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();


        DJGroup g9 = gbbrigada.setCriteriaColumn((PropertyColumn) columnabrigada)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g10 = gbgrupo.setCriteriaColumn((PropertyColumn) columnagrupo)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g11 = gbcuadrilla.setCriteriaColumn((PropertyColumn) columnacuadrilla)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }

            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);


        } else if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);

        } else if (niveles == 7) {
            drb.addGroup(g1);
            drb.addGroup(g9);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 8) {
            drb.addGroup(g1);
            drb.addGroup(g9);
            drb.addGroup(g10);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);

        } else if (niveles == 9) {
            drb.addGroup(g1);
            drb.addGroup(g9);
            drb.addGroup(g10);
            drb.addGroup(g11);
            drb.addGroup(g7);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacuadrilla);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 73) {
            drb.addGroup(g1);
            drb.addGroup(g9);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columnabrigada);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 15) {
            drb.addGroup(g5);
            drb.addGroup(g7);

            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }

    /**
     * Para el consolidado de manos de obra y materiales
     */
    public DynamicReport cuantitativaReportConsolidado(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/reportCuantitativaCert.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnascala = ColumnBuilder.getNew()
                .setColumnProperty("grupoScala", String.class.getName())
                .setTitle("GES: ").setWidth(40)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gb8 = new GroupBuilder();


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)

                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnatipo)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) columnascala)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columnatipo);
            drb.addColumn(columnascala);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }

            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);


        }

        if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);
            drb.addGroup(g8);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columnatipo);
            drb.addColumn(columnascala);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g7);
            drb.addGroup(g8);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columnatipo);
            drb.addColumn(columnascala);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columnatipo);
            drb.addColumn(columnascala);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);
            drb.addColumn(columnascala);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            // drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columnatipo);
            drb.addColumn(columnascala);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            //  drb.addColumn(columnapeso);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }

        if (niveles == 55) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columnatipo);
            drb.addColumn(columnascala);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        }


        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }


    public DynamicReport cuantitativaReportUOGROUP(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setGrandTotalLegendStyle(headerVariables)
                .setGrandTotalLegend("Costo Total de Recursos")
                .setTemplateFile("templete/reportPresupuestoCuantitativa.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("datosFondoHorarioExplotacionModels", ArrayList.class.getName());
        DynamicReport drFooterSubreportFHE = createFooterFondoHorarioSubreport();
        drb.addConcatenatedReport(drFooterSubreportFHE, new ClassicLayoutManager(), "datosFondoHorarioExplotacionModels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);

        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopuo = ColumnBuilder.getNew()
                .setColumnProperty("descripUO", String.class.getName())
                .setTitle("UO: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        Style glabelStyle3 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Subtotal " + fields.get("tipo") + ":";
            }
        }, glabelStyle2, null);


        DJGroupLabel glabelTipo = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Costo Directo: ";
            }
        }, glabelStyle3, null);


        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gb8 = new GroupBuilder();


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopuo)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabelTipo)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) columnatipo)
                .addVariable("horas1", columnacantidad, DJCalculation.SUM)
                .addFooterVariable(columnacantidad, new CustomExpression() {
                    @Override
                    public Object evaluate(Map fields, Map variabled, Map map2) {
                        Double var = 0.0;
                        if (fields.get("tipo").toString().trim().equals("Equipos")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Mano de Obra")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Materiales")) {
                            var = null;
                        }
                        return var;
                    }

                    @Override
                    public String getClassName() {
                        return Double.class.getName();
                    }
                }, headerVariables)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();

        drb.addGlobalFooterVariable(columnacosto, DJCalculation.SUM, headerVariables);


        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);

        } else if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);
            drb.addGroup(g8);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g7);
            drb.addGroup(g8);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 15) {
            drb.addGroup(g1);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 125) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 1235) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 12345) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 156) {
            drb.addGroup(g1);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 1256) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 12356) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 123456) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        }
        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();


        return dr;

    }


    public DynamicReport cuantitativaReportRVGROUP(ReportesController reportes, int niveles, boolean cuc) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setGrandTotalLegendStyle(headerVariables)
                .setGrandTotalLegend("Costo Total de Recursos")
                .setTemplateFile("templete/reportPresupuestoCuantitativa.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idEmpresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbidE = new GroupBuilder();
        DJGroup g1Id = gbidE.setCriteriaColumn((PropertyColumn) id)
                .setGroupLayout(GroupLayout.EMPTY)
                .build();

        drb.addColumn(id);
        drb.addGroup(g1Id);

        drb.addField("datosFondoHorarioExplotacionModels", ArrayList.class.getName());
        DynamicReport drFooterSubreportFHE = createFooterFondoHorarioSubreport();
        drb.addConcatenatedReport(drFooterSubreportFHE, new ClassicLayoutManager(), "datosFondoHorarioExplotacionModels", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Sub: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopuo = ColumnBuilder.getNew()
                .setColumnProperty("descripUO", String.class.getName())
                .setTitle(" ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("Tipo").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciomlc = ColumnBuilder.getNew()
                .setColumnProperty("preciomlc", Double.class.getName())
                .setTitle("MLC").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacosto = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Valor").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        Style glabelStyle3 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Subtotal " + fields.get("tipo") + ":";
            }
        }, glabelStyle2, null);


        DJGroupLabel glabelTipo = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Costo Directo: ";
            }
        }, glabelStyle3, null);

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();
        GroupBuilder gb8 = new GroupBuilder();


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopuo)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabelTipo)
                .setFooterVariablesHeight(12)
                .build();

        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) columnatipo)
                .addVariable("horas1", columnacantidad, DJCalculation.SUM)
                .addFooterVariable(columnacantidad, new CustomExpression() {
                    @Override
                    public Object evaluate(Map fields, Map variabled, Map map2) {
                        Double var = 0.0;
                        if (fields.get("tipo").toString().trim().equals("Equipos")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Mano de Obra")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Materiales")) {
                            var = null;
                        }
                        return var;
                    }

                    @Override
                    public String getClassName() {
                        return Double.class.getName();
                    }
                }, headerVariables)
                .addFooterVariable(columnacosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();


        drb.addGlobalFooterVariable(columnacosto, DJCalculation.SUM, headerVariables);


        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);

        } else if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);
            drb.addGroup(g8);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g7);
            drb.addGroup(g8);


            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);


            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            drb.addColumn(columnacosto);
        } else if (niveles == 15) {
            drb.addGroup(g1);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 125) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 1235) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 12345) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 156) {
            drb.addGroup(g1);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 1256) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 12356) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        } else if (niveles == 123456) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);
            drb.addGroup(g8);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopuo);
            drb.addColumn(columnatipo);

            drb.addColumn(columnacode);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnaprecio);
            if (cuc == true) {
                drb.addColumn(columnapreciomlc);
            }
            drb.addColumn(columnacosto);
        }
        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();


        return dr;

    }


    private DynamicReport createFooterFondoHorarioSubreport() {
        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        // headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(14, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (10);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setTitle("Costo de Mano de Obra en Equipos")
                .setGrandTotalLegend("Total")
                .setUseFullPageWidth(true)
                .setOddRowBackgroundStyle(oddRowStyle);


        AbstractColumn code = ColumnBuilder.getNew()
                .setColumnProperty("code", String.class.getName())
                .setTitle("Código").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn descrip = ColumnBuilder.getNew()
                .setColumnProperty("descrip", String.class.getName())
                .setTitle("Descripción").setWidth(52)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn fhepp = ColumnBuilder.getNew()
                .setColumnProperty("fhpp", Double.class.getName())
                .setTitle("H/E").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn cpo = ColumnBuilder.getNew()
                .setColumnProperty("tarifa", Double.class.getName())
                .setTitle("Tarifa M.O").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn cpe = ColumnBuilder.getNew()
                .setColumnProperty("salario", Double.class.getName())
                .setTitle("Costo Total").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        /*
        AbstractColumn cet = ColumnBuilder.getNew()
                .setColumnProperty("cet", Double.class.getName())
                .setTitle("C.E.T").setWidth(10)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn otra = ColumnBuilder.getNew()
                .setColumnProperty("otra", Double.class.getName())
                .setTitle("Otra").setWidth(10)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn fhe = ColumnBuilder.getNew()
                .setColumnProperty("fhe", Double.class.getName())
                .setTitle("F.H.E").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn fhs = ColumnBuilder.getNew()
                .setColumnProperty("fhs", Double.class.getName())
                .setTitle("F.H.S").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
*/
        drb.addColumn(code);
        drb.addColumn(descrip);
        drb.addColumn(fhepp);
        drb.addColumn(cpo);
        drb.addColumn(cpe);
        // drb.addColumn(cet);
        // drb.addColumn(otra);
        // drb.addColumn(fhe);
        // drb.addColumn(fhs);

        drb.addGlobalFooterVariable(fhepp, DJCalculation.SUM, headerStyle);
        drb.addGlobalFooterVariable(cpe, DJCalculation.SUM, headerStyle);

        DynamicReport dr = drb.build();


        return dr;

    }

    public DynamicReport salarioReport(ReportesController reportes, int niveles) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8Letter.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Vol.").setWidth(13)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("horas", Double.class.getName())
                .setTitle("HH").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("salario", Double.class.getName())
                .setTitle("Salario").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(columnacode);
        drb.addColumn(columnadescrip);
        drb.addColumn(columnaum);
        drb.addColumn(columnatipo);
        drb.addColumn(columnacantidad);
        drb.addColumn(columnaprecio);

        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Especialidad: " + fields.get("especialidad");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Subespecialidad: " + fields.get("subespecialidad");
            }
        }, titleStyle, null);

        DJGroupLabel glabelUO = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total UO: ";
            }
        }, titleStyle, null);


        if (niveles == 1) {
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columngruopEmpresa);
            drb.addGroup(g1);


        } else if (niveles == 2) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g2);


        } else if (niveles == 3) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);


            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);


        } else if (niveles == 4) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g4);

        } else if (niveles == 5) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);

        } else if (niveles == 6) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);
            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g6);
        } else if (niveles == 15) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);
        } else if (niveles == 125) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);
        } else if (niveles == 1235) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);
        } else if (niveles == 156) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);
            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);
            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g6);
        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }

    public DynamicReport salarioUORVReport(ReportesController reportes, int niveles) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8Letter.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacodeUO = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codeRv", String.class.getName())
                .setTitle("Codigo").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descriprv", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("umrv", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("cantidadrv", Double.class.getName())
                .setTitle("Vol.").setWidth(13)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("horas", Double.class.getName())
                .setTitle("HH").setWidth(13).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("salario", Double.class.getName())
                .setTitle("Salario").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(columnacode);
        drb.addColumn(columnadescrip);
        drb.addColumn(columnaum);
        drb.addColumn(columnatipo);
        drb.addColumn(columnacantidad);
        drb.addColumn(columnaprecio);

        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Especialidad: " + fields.get("especialidad");
            }
        }, titleStyle, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Subespecialidad: " + fields.get("subespecialidad");
            }
        }, titleStyle, null);

        DJGroupLabel glabelUO = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total UO: ";
            }
        }, titleStyle, null);

        if (niveles == 1) {
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columngruopEmpresa);
            drb.addGroup(g1);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g2);


        } else if (niveles == 2) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g3);


        } else if (niveles == 3) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);


            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g4);


        } else if (niveles == 4) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g5);

        } else if (niveles == 5) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);


            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g6);

        } else if (niveles == 6) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g4);
            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);
            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g6);

            GroupBuilder gb7 = new GroupBuilder();
            DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g7);
        } else if (niveles == 15) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g6);

        } else if (niveles == 125) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruoesp);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g6);
        } else if (niveles == 1235) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruoesp);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g6);
        } else if (niveles == 156) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g1);
            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g5);
            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(10)
                    .build();
            drb.addGroup(g6);

            GroupBuilder gb7 = new GroupBuilder();
            DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnacodeUO)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, null)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, null)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setFooterLabel(glabelUO)
                    .setFooterVariablesHeight(10)
                    .build();

            drb.addColumn(columnacodeUO);
            drb.addGroup(g7);
        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();


        return dr;

    }

    public DynamicReport CertificoUoReport(ReportesController reportes, int niveles, boolean check, double coef) throws ClassNotFoundException {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8Letter.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopBrigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada: ").setWidth(45)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopGrupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopCuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(45)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(38)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacodeUO = ColumnBuilder.getNew()
                .setColumnProperty("uoCode", String.class.getName())
                .setTitle("Código").setWidth(16)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("uoDescrip", String.class.getName())
                .setTitle("Descripción").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("certVolum", Double.class.getName())
                .setTitle("Vol.").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("hhAndHE", Double.class.getName())
                .setTitle("HH y HE").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("salariomn", Double.class.getName())
                .setTitle("Salario").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaprecioInc = ColumnBuilder.getNew()
                .setColumnProperty("salarioInc", Double.class.getName())
                .setTitle("Sal. Inc").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnapreciocuost = ColumnBuilder.getNew()
                .setColumnProperty("costo", Double.class.getName())
                .setTitle("Costo").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(columnacodeUO);
        drb.addColumn(columnadescrip);
        drb.addColumn(columnaum);
        drb.addColumn(columnatipo);
        drb.addColumn(columnacantidad);
        drb.addColumn(columnaprecio);
        drb.addColumn(columnaprecioInc);
        drb.addColumn(columnapreciocuost);

        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel1 = new DJGroupLabel("HH y HE", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Salario", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Sal. Inc", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Costo", glabelStyle);


        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3B = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Brigada: " + fields.get("brigada");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabelG = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Grupo: " + fields.get("grupo");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabelC = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Cuadrilla: " + fields.get("cuadrilla");
            }
        }, glabelStyle2, null);

        if (niveles == 4) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopBrigada)
                    .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3B)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addColumn(columngruopBrigada);
            drb.addGroup(g2);

            if (check == true) {
                GroupBuilder gb3 = new GroupBuilder();
                DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopGrupo)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabelG)
                        .setFooterVariablesHeight(12)
                        .build();

                drb.addColumn(columngruopGrupo);
                drb.addGroup(g3);

                GroupBuilder gb5 = new GroupBuilder();
                DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruopZona)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabel3Z)
                        .setFooterVariablesHeight(12)
                        .build();

                drb.addGroup(g5);

                GroupBuilder gb6 = new GroupBuilder();
                DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopObj)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabel3O)
                        .setFooterVariablesHeight(12)
                        .build();
                drb.addGroup(g6);

                GroupBuilder gb7 = new GroupBuilder();
                DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopNiv)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabel3N)
                        .setFooterVariablesHeight(12)
                        .build();
                drb.addGroup(g7);


            } else if (check == false) {

                GroupBuilder gb3 = new GroupBuilder();
                DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopGrupo)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabelG)
                        .setFooterVariablesHeight(12)
                        .build();

                drb.addColumn(columngruopGrupo);
                drb.addGroup(g3);

                GroupBuilder gb4 = new GroupBuilder();
                DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopCuadrilla)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabelC)
                        .setFooterVariablesHeight(12)
                        .build();

                drb.addColumn(columngruopCuadrilla);
                drb.addGroup(g4);

                GroupBuilder gb5 = new GroupBuilder();
                DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruopZona)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabel3Z)
                        .setFooterVariablesHeight(12)
                        .build();

                drb.addGroup(g5);

                GroupBuilder gb6 = new GroupBuilder();
                DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopObj)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabel3O)
                        .setFooterVariablesHeight(12)
                        .build();
                drb.addGroup(g6);

                GroupBuilder gb7 = new GroupBuilder();
                DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopNiv)
                        .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                        .addFooterVariable(columnaprecio, DJCalculation.SUM, headerVariables, null, glabel2)
                        .addFooterVariable(columnaprecioInc, DJCalculation.SUM, headerVariables, null, glabel3)
                        .addFooterVariable(columnapreciocuost, DJCalculation.SUM, headerVariables, null, glabel4)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                        .setFooterLabel(glabel3N)
                        .setFooterVariablesHeight(12)
                        .build();
                drb.addGroup(g7);

            }
        }
        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }

    public DynamicReport createConveniosReport(ReportesController reportes, int niveles, String name, double coef) {
        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);

        Style titleStyle1 = new Style();
        titleStyle1.setFont(new Font(14, Font._FONT_ARIAL, true));
        titleStyle1.setHorizontalAlign(HorizontalAlign.LEFT);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle1)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8LandScapeConvenios.jrxml");


        AbstractColumn empresa = ColumnBuilder.getNew()
                .setColumnProperty("nameEmp", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(empresa);
        drb.addGroup(g1);

        AbstractColumn brigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gb2 = new GroupBuilder();
        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) brigada)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(brigada);
        drb.addGroup(g2);


        AbstractColumn grupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gb3 = new GroupBuilder();
        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) grupo)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(grupo);
        drb.addGroup(g3);

        AbstractColumn cuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla: ").setWidth(55)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gb4 = new GroupBuilder();
        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) cuadrilla)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(cuadrilla);
        drb.addGroup(g4);


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("idCuadrilla", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        drb.addField("datosTableConveniosArrayList", ArrayList.class.getName());

        DynamicReport drFooterSubreport = createFooterSubreportStaticTable();
        drb.addSubreportInGroupFooter(4, drFooterSubreport, new ClassicLayoutManager(),
                "datosTableConveniosArrayList", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);
        drb.addField("datosUoPlanCertConvModelArrayList", ArrayList.class.getName());

        DynamicReport drFooterSubreport2 = createFooterSubreportPlanCert(coef);
        drb.addSubreportInGroupFooter(4, drFooterSubreport2, new ClassicLayoutManager(),
                "datosUoPlanCertConvModelArrayList", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);
        drb.addField("datosRecursosConvenioModelArrayList", ArrayList.class.getName());


        DynamicReport drFooterSubreport3 = createFooterSubreportPlanCertRec(name);
        //drb.addConcatenatedReport(drFooterSubreport3, new ClassicLayoutManager(),
        //      "datosRecursosConvenioModelArrayList", DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, true);


        drb.setUseFullPageWidth(true);

        DynamicReport dr = drb.build();


        return dr;

    }

    private DynamicReport createFooterSubreportStaticTable() {


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);
        detailStyle.setBorderBottom(Border.PEN_1_POINT());
        detailStyle.setBorderTop(Border.PEN_1_POINT());
        detailStyle.setBorderLeft(Border.PEN_1_POINT());
        detailStyle.setBorderRight(Border.PEN_1_POINT());

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBorderLeft(Border.PEN_1_POINT());
        headerStyle.setBorderRight(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (10);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setTemplateFile("templete/reportA4.jrxml")
                .setOddRowBackgroundStyle(oddRowStyle);


        AbstractColumn cInt = ColumnBuilder.getNew()
                .setColumnProperty("codeInt", String.class.getName())
                .setTitle("No. Interno").setWidth(100)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn name = ColumnBuilder.getNew()
                .setColumnProperty("fullName", String.class.getName())
                .setTitle("Nombre y Apellidos").setWidth(550)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn th = ColumnBuilder.getNew()
                .setColumnProperty("th", String.class.getName())
                .setTitle("TH").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn hp = ColumnBuilder.getNew()
                .setColumnProperty("hp", String.class.getName())
                .setTitle("HP").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn firma = ColumnBuilder.getNew()
                .setColumnProperty("firma", String.class.getName())
                .setTitle("Firma").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn hr = ColumnBuilder.getNew()
                .setColumnProperty("hr", String.class.getName())
                .setTitle("HR").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn cpl = ColumnBuilder.getNew()
                .setColumnProperty("cpl", String.class.getName())
                .setTitle("CPL").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn firma2 = ColumnBuilder.getNew()
                .setColumnProperty("firma2", String.class.getName())
                .setTitle("Firma").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn observ = ColumnBuilder.getNew()
                .setColumnProperty("observaciones", String.class.getName())
                .setTitle("Observaciones").setWidth(250)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(cInt);
        drb.addColumn(name);
        drb.addColumn(th);
        drb.addColumn(hp);
        drb.addColumn(firma);
        drb.addColumn(hr);
        drb.addColumn(cpl);
        drb.addColumn(firma2);
        drb.addColumn(observ);

        drb.setUseFullPageWidth(true);

        DynamicReport dr = drb.build();


        return dr;
    }

    private DynamicReport createFooterSubreportPlanCert(double coef) {


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);
        detailStyle.setBorderBottom(Border.PEN_1_POINT());
        detailStyle.setBorderTop(Border.PEN_1_POINT());
        detailStyle.setBorderLeft(Border.PEN_1_POINT());
        detailStyle.setBorderRight(Border.PEN_1_POINT());
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBorderLeft(Border.PEN_1_POINT());
        headerStyle.setBorderRight(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(6, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (10);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setGrandTotalLegend("Totales")
                .setTemplateFile("templete/reportA4.jrxml")
                .setOddRowBackgroundStyle(oddRowStyle);


        AbstractColumn cInt = ColumnBuilder.getNew()
                .setColumnProperty("uoCode", String.class.getName())
                .setTitle("Código").setWidth(90)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn name = ColumnBuilder.getNew()
                .setColumnProperty("uoDescrip", String.class.getName())
                .setTitle("Descripción").setWidth(95)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn th = ColumnBuilder.getNew()
                .setColumnProperty("plnVolumen", Double.class.getName())
                .setTitle("Vol.").setWidth(40).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn hp = ColumnBuilder.getNew()
                .setColumnProperty("plnHoras", Double.class.getName())
                .setTitle("Horas").setWidth(40).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn firma = ColumnBuilder.getNew()
                .setColumnProperty("plnSalario", Double.class.getName())
                .setTitle("Sal.").setWidth(35).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn firmaInc = ColumnBuilder.getNew()
                .setColumnProperty("plnSalarioInc", Double.class.getName())
                .setTitle("Sal. Inc").setWidth(40).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn hr = ColumnBuilder.getNew()
                .setColumnProperty("plnProduccion", Double.class.getName())
                .setTitle("Prod").setWidth(40).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn cpl = ColumnBuilder.getNew()
                .setColumnProperty("crtVolumen", Double.class.getName())
                .setTitle("Vol.").setWidth(40).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn firma2 = ColumnBuilder.getNew()
                .setColumnProperty("crtHoras", Double.class.getName())
                .setTitle("Horas").setWidth(40).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn observ = ColumnBuilder.getNew()
                .setColumnProperty("crtSalario", Double.class.getName())
                .setTitle("Sal.").setWidth(35).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn firmaCertInc = ColumnBuilder.getNew()
                .setColumnProperty("crtSalarioInc", Double.class.getName())
                .setTitle("Sal. Inc").setWidth(40).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn prod = ColumnBuilder.getNew()
                .setColumnProperty("crtProduccion", Double.class.getName())
                .setTitle("Prod").setWidth(40).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn aprob = ColumnBuilder.getNew()
                .setColumnProperty("aprobado", String.class.getName())
                .setTitle("Aprob.").setWidth(40)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(cInt);
        drb.addColumn(name);
        drb.addColumn(th);
        drb.addColumn(hp);
        drb.addColumn(firma);
        drb.addColumn(firmaInc);
        drb.addColumn(hr);
        drb.addColumn(cpl);
        drb.addColumn(firma2);
        drb.addColumn(observ);
        drb.addColumn(firmaCertInc);
        drb.addColumn(prod);
        drb.addColumn(aprob);


        drb.setColspan(2, 6, "Plan");
        drb.setColspan(7, 6, "Real");

        drb.addGlobalFooterVariable(hp, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(firma, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(hr, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(firma2, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(firmaInc, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(observ, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(firmaCertInc, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(prod, DJCalculation.SUM, headerVariables);

        drb.setUseFullPageWidth(true);

        DynamicReport dr = drb.build();


        return dr;
    }

    public DynamicReport createFooterSubreportPlanCertRec(String nameRep) {


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setTitle(nameRep)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setGrandTotalLegend("Total Horas Cuadrilla")
                .setTemplateFile("templete/blankTemplate.jrxml")
                .setOddRowBackgroundStyle(oddRowStyle);

        AbstractColumn obra = ColumnBuilder.getNew()
                .setColumnProperty("obra", String.class.getName())
                .setTitle("Obra: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gbO = new GroupBuilder();
        DJGroup g1B = gbO.setCriteriaColumn((PropertyColumn) obra)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(obra);
        drb.addGroup(g1B);


        AbstractColumn empresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        GroupBuilder gb1 = new GroupBuilder();
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) empresa)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(empresa);
        drb.addGroup(g1);

        AbstractColumn brigada = ColumnBuilder.getNew()
                .setColumnProperty("brigada", String.class.getName())
                .setTitle("Brigada: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gb2 = new GroupBuilder();
        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) brigada)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(brigada);
        drb.addGroup(g2);


        AbstractColumn grupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Grupo: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gb3 = new GroupBuilder();
        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) grupo)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(grupo);
        drb.addGroup(g3);

        AbstractColumn cuadrilla = ColumnBuilder.getNew()
                .setColumnProperty("cuadrilla", String.class.getName())
                .setTitle("Cuadrilla: ").setWidth(55)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        GroupBuilder gb4 = new GroupBuilder();
        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) cuadrilla)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(cuadrilla);
        drb.addGroup(g4);


        AbstractColumn cInt = ColumnBuilder.getNew()
                .setColumnProperty("recCode", String.class.getName())
                .setTitle("Código").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn name = ColumnBuilder.getNew()
                .setColumnProperty("recDescrip", String.class.getName())
                .setTitle("Descripción").setWidth(200)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn th = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(20)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn hr = ColumnBuilder.getNew()
                .setColumnProperty("horascert", Double.class.getName())
                .setTitle("HH y HE Salario ").setWidth(50).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn cpl = ColumnBuilder.getNew()
                .setColumnProperty("horaSalariocert", Double.class.getName())
                .setTitle("HH Salario").setWidth(40).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(cInt);
        drb.addColumn(name);
        drb.addColumn(th);
        drb.addColumn(hr);
        drb.addColumn(cpl);

        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel4 = new DJGroupLabel("Total Horas", glabelStyle);
        AbstractColumn tipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("  ").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(tipo);

        GroupBuilder gb5 = new GroupBuilder();
        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) tipo)
                .addFooterVariable(hr, DJCalculation.SUM, headerVariables, null, glabel4)
                .addFooterVariable(cpl, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();
        drb.addGroup(g5);

        drb.addGlobalFooterVariable(cpl, DJCalculation.SUM, headerVariables);
        drb.setUseFullPageWidth(true);

        DynamicReport dr = drb.build();


        return dr;
    }


    public DynamicReport createCalcSalarioImpacto(ReportesController reportes, int niveles) throws ClassNotFoundException {

        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/reportCalculoSalImpacto.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopUnidad = ColumnBuilder.getNew()
                .setColumnProperty("unidad", String.class.getName())
                .setTitle("UO: ").setWidth(30)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("recurso", String.class.getName())
                .setTitle("Código").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(45)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnagrupo = ColumnBuilder.getNew()
                .setColumnProperty("grupo", String.class.getName())
                .setTitle("Escala").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnat30 = ColumnBuilder.getNew()
                .setColumnProperty("tar30", Double.class.getName())
                .setTitle("Precons").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnasal30 = ColumnBuilder.getNew()
                .setColumnProperty("tar30cal", Double.class.getName())
                .setTitle("Sal. 30").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnat15 = ColumnBuilder.getNew()
                .setColumnProperty("tar15", Double.class.getName())
                .setTitle("R15").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnasal15 = ColumnBuilder.getNew()
                .setColumnProperty("tar15cal", Double.class.getName())
                .setTitle("Sal. 15").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaimpacto = ColumnBuilder.getNew()
                .setColumnProperty("impacto", Double.class.getName())
                .setTitle("Impacto").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();


        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();

        DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                //.addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("HH", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Sal. 30", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Sal. 15", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Impacto", glabelStyle);

        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopUnidad)
                .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnasal30, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnasal15, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaimpacto, DJCalculation.SUM, headerVariables, null, glabel4)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS)
                .build();

        if (niveles == 1) {
            drb.addGroup(g1);
            drb.addGroup(g7);
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnat30);
            drb.addColumn(columnasal30);
            drb.addColumn(columnat15);
            drb.addColumn(columnasal15);
            drb.addColumn(columnaimpacto);

        } else if (niveles == 2) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnat30);
            drb.addColumn(columnasal30);
            drb.addColumn(columnat15);
            drb.addColumn(columnasal15);
            drb.addColumn(columnaimpacto);
        } else if (niveles == 3) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);

            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnat30);
            drb.addColumn(columnasal30);
            drb.addColumn(columnat15);
            drb.addColumn(columnasal15);
            drb.addColumn(columnaimpacto);
        } else if (niveles == 4) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);

            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnat30);
            drb.addColumn(columnasal30);
            drb.addColumn(columnat15);
            drb.addColumn(columnasal15);
            drb.addColumn(columnaimpacto);
        } else if (niveles == 5) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);

            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnat30);
            drb.addColumn(columnasal30);
            drb.addColumn(columnat15);
            drb.addColumn(columnasal15);
            drb.addColumn(columnaimpacto);
        } else if (niveles == 6) {
            drb.addGroup(g1);
            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addGroup(g7);

            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            drb.addColumn(columngruopUnidad);
            drb.addColumn(columnauo);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnagrupo);
            drb.addColumn(columnacantidad);
            drb.addColumn(columnat30);
            drb.addColumn(columnasal30);
            drb.addColumn(columnat15);
            drb.addColumn(columnasal15);
            drb.addColumn(columnaimpacto);
        }
        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }


    public DynamicReport createObraRenglonReport(ReportesController reportes, int niveles) {

        reportesController = reportes;

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/reportCuantitativaCert.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columncoderv = ColumnBuilder.getNew()
                .setColumnProperty("codigo", String.class.getName())
                .setTitle("Código").setWidth(15)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(45)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("cant", Double.class.getName())
                .setTitle("Volumen").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columntotal = ColumnBuilder.getNew()
                .setColumnProperty("costoTotal", Double.class.getName())
                .setTitle("Costo RV").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();
        GroupBuilder gb5 = new GroupBuilder();
        GroupBuilder gb6 = new GroupBuilder();
        GroupBuilder gb7 = new GroupBuilder();

        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Total", glabelStyle);


        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columntotal, DJCalculation.SUM, headerVariables, null, glabel1)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();


        if (niveles == 1) {
            drb.addColumn(columncoderv);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columntotal);

        } else if (niveles == 2) {
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columntotal, DJCalculation.SUM, headerVariables, null, glabel1)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);
            drb.addColumn(columngruopZona);
            drb.addColumn(columncoderv);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columntotal);

        } else if (niveles == 3) {
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columntotal, DJCalculation.SUM, headerVariables, null, glabel1)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            drb.addColumn(columncoderv);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columntotal);
        } else if (niveles == 4) {
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .addFooterVariable(columntotal, DJCalculation.SUM, headerVariables, null, glabel1)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            drb.addColumn(columncoderv);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columntotal);
        } else if (niveles == 5) {
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columntotal, DJCalculation.SUM, headerVariables, null, glabel1)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            drb.addColumn(columncoderv);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columntotal);
        } else if (niveles == 6) {
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                    .addFooterVariable(columntotal, DJCalculation.SUM, headerVariables, null, glabel1)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);
            drb.addGroup(g3);
            drb.addGroup(g4);
            drb.addGroup(g5);
            drb.addGroup(g6);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);

            drb.addColumn(columncoderv);
            drb.addColumn(columnadescrip);
            drb.addColumn(columnaum);
            drb.addColumn(columnacantidad);
            drb.addColumn(columntotal);
        }


        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }

    public DynamicReport ResumenHHReport() throws ClassNotFoundException {
        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8LandScape.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(45)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(38)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subespecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(90)
                .setStyle(titleStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacodeUO = ColumnBuilder.getNew()
                .setColumnProperty("hhCant", Double.class.getName())
                .setTitle("HH Presup").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("heCant", Double.class.getName())
                .setTitle("HE Presup").setWidth(15).setPattern("0.0")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("hhCert", Double.class.getName())
                .setTitle("HH Cert").setWidth(15).setPattern("0.0")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum2 = ColumnBuilder.getNew()
                .setColumnProperty("heCert", Double.class.getName())
                .setTitle("HE Cert").setWidth(15).setPattern("0.0")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnatipo = ColumnBuilder.getNew()
                .setColumnProperty("hhDisp", Double.class.getName())
                .setTitle("HH Disp").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("heDisp", Double.class.getName())
                .setTitle("HE Disp").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(columngruopsup);
        drb.addColumn(columnacodeUO);
        drb.addColumn(columnadescrip);
        drb.addColumn(columnaum);
        drb.addColumn(columnaum2);
        drb.addColumn(columnatipo);
        drb.addColumn(columnacantidad);


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel1 = new DJGroupLabel("HH Presup", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("HE Presup", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("HH Cert", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("HE Cert", glabelStyle);
        DJGroupLabel glabel5 = new DJGroupLabel("HH Disp", glabelStyle);
        DJGroupLabel glabel6 = new DJGroupLabel("HE Disp", glabelStyle);


        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();


        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Zona: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Objeto: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Nivel: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total Especialidad: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);


        drb.addColumn(columngruopEmpresa);
        drb.addColumn(columngruopZona);
        drb.addColumn(columngruopObj);
        drb.addColumn(columngruopNiv);
        drb.addColumn(columngruoesp);

        GroupBuilder gb1 = new GroupBuilder();
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .addFooterVariable(columnacodeUO, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnadescrip, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaum, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaum2, DJCalculation.SUM, headerVariables, null, glabel4)
                .addFooterVariable(columnatipo, DJCalculation.SUM, headerVariables, null, glabel5)
                .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel6)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();
        drb.addGroup(g1);

        GroupBuilder gb5 = new GroupBuilder();
        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruopZona)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .addFooterVariable(columnacodeUO, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnadescrip, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaum, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaum2, DJCalculation.SUM, headerVariables, null, glabel4)
                .addFooterVariable(columnatipo, DJCalculation.SUM, headerVariables, null, glabel5)
                .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel6)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Z)
                .setFooterVariablesHeight(12)
                .build();
        drb.addGroup(g5);

        GroupBuilder gb6 = new GroupBuilder();
        DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopObj)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .addFooterVariable(columnacodeUO, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnadescrip, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaum, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaum2, DJCalculation.SUM, headerVariables, null, glabel4)
                .addFooterVariable(columnatipo, DJCalculation.SUM, headerVariables, null, glabel5)
                .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel6)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3O)
                .setFooterVariablesHeight(12)
                .build();
        drb.addGroup(g6);

        GroupBuilder gb7 = new GroupBuilder();
        DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columngruopNiv)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .addFooterVariable(columnacodeUO, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnadescrip, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaum, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaum2, DJCalculation.SUM, headerVariables, null, glabel4)
                .addFooterVariable(columnatipo, DJCalculation.SUM, headerVariables, null, glabel5)
                .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel6)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3N)
                .setFooterVariablesHeight(12)
                .build();
        drb.addGroup(g7);

        GroupBuilder gb8 = new GroupBuilder();
        DJGroup g8 = gb8.setCriteriaColumn((PropertyColumn) columngruoesp)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .addFooterVariable(columnacodeUO, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnadescrip, DJCalculation.SUM, headerVariables, null, glabel2)
                .addFooterVariable(columnaum, DJCalculation.SUM, headerVariables, null, glabel3)
                .addFooterVariable(columnaum2, DJCalculation.SUM, headerVariables, null, glabel4)
                .addFooterVariable(columnatipo, DJCalculation.SUM, headerVariables, null, glabel5)
                .addFooterVariable(columnacantidad, DJCalculation.SUM, headerVariables, null, glabel6)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3Es)
                .setFooterVariablesHeight(12)
                .build();
        drb.addGroup(g8);


        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }

    public DynamicReport createreportTarifaTotal() throws ClassNotFoundException {
        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.JUSTIFIED);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                // .setGrandTotalLegend("Total Costos Directos")
                .setOddRowBackgroundStyle(oddRowStyle)
                .setPrintColumnNames(false)
                .setTemplateFile("templete/reportTarifaLetter.jrxml");


        /**
         * grupos en el reporte
         */

        AbstractColumn colgTipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle("").setWidth(10)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();
        /**
         * Contenido de la table
         */

        /*
        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("code", String.class.getName())
                .setTitle("Código").setWidth(15)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
*/
        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("description", String.class.getName())
                .setTitle("Descripción").setWidth(60)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(importeStyle).setHeaderStyle(headerStyle)
                .build();

/*
        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Rendimiento").setWidth(15).setPattern("0.0000")
                .setStyle(importeStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacostUnitario = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("C.Unitario").setWidth(14).setPattern("0.00")
                .setStyle(importeStyle).setHeaderStyle(headerStyle)
                .build();
*/
        AbstractColumn materiales = ColumnBuilder.getNew()
                .setColumnProperty("costo", Double.class.getName())
                .setTitle("Importe").setWidth(17).setPattern("0.00")
                .setStyle(importeStyle).setHeaderStyle(headerStyle)
                .build();


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        // drb.addColumn(columnacode);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        // drb.addColumn(columnacant);
        // drb.addColumn(columnacostUnitario);
        drb.addColumn(materiales);

        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Costo Total: " + fields.get("tipo");
            }
        }, glabelStyle, null);


        GroupBuilder gb1 = new GroupBuilder();
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) colgTipo)
                .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();
        drb.addGroup(g1);
        drb.addColumn(colgTipo);
        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }

    public DynamicReport createreportRCDetail(TarifaSalarial tarifaSalarial) throws ClassNotFoundException {
        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.JUSTIFIED);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style headerVariablesTotal = new Style();
        headerVariablesTotal.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariablesTotal.setBorderTop(Border.THIN());
        headerVariablesTotal.setPaddingTop(50);
        headerVariablesTotal.setHorizontalAlign(HorizontalAlign.CENTER);
        headerVariablesTotal.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariablesTotal)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setGrandTotalLegend("Costo Directo Total" + "(" + tarifaSalarial.getMoneda() + ")")
                .setOddRowBackgroundStyle(oddRowStyle)
                .setPrintColumnNames(false)
                .setTemplateFile("templete/report8RCLetter.jrxml");


        /**
         * grupos en el reporte
         */

        AbstractColumn colgTipo = ColumnBuilder.getNew()
                .setColumnProperty("tipo", String.class.getName())
                .setTitle(" * ").setWidth(10)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();
        /**
         * Contenido de la table
         */

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("code", String.class.getName())
                .setTitle("Código").setWidth(25)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("description", String.class.getName())
                .setTitle("Descripción").setWidth(60)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(importeStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Rendimiento").setWidth(17).setPattern("0.0000")
                .setStyle(importeStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacostUnitario = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("C.Unitario" + "(" + tarifaSalarial.getMoneda() + ")").setWidth(14).setPattern("0.00")
                .setStyle(importeStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn materiales = ColumnBuilder.getNew()
                .setColumnProperty("costo", Double.class.getName())
                .setTitle("Importe" + "(" + tarifaSalarial.getMoneda() + ")").setWidth(14).setPattern("0.00")
                .setStyle(importeStyle).setHeaderStyle(headerStyle)
                .build();


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.CENTER).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        drb.addColumn(columnacode);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        drb.addColumn(columnacant);
        drb.addColumn(columnacostUnitario);
        drb.addColumn(materiales);

        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Subtotal: " + fields.get("tipo");
            }
        }, glabelStyle, null);


        GroupBuilder gb1 = new GroupBuilder();
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) colgTipo)
                .addVariable("horas1", columnacant, DJCalculation.SUM)
                .addFooterVariable(columnacant, new CustomExpression() {
                    @Override
                    public Object evaluate(Map fields, Map variabled, Map map2) {
                        Double var = 0.0;
                        if (fields.get("tipo").toString().trim().equals("Equipos")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Mano de Obra")) {
                            var += (Double) variabled.get("horas1");
                        } else if (fields.get("tipo").toString().trim().equals("Materiales")) {
                            var = null;
                        }
                        return var;
                    }

                    @Override
                    public String getClassName() {
                        return Double.class.getName();
                    }
                }, headerVariables)
                .addFooterVariable(materiales, DJCalculation.SUM, headerVariables, null, null)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .setFooterLabel(glabel3E)
                .setFooterVariablesHeight(12)
                .build();
        drb.addGroup(g1);
        drb.addColumn(colgTipo);

        drb.addGlobalFooterVariable(materiales, DJCalculation.SUM, headerVariablesTotal);
        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }

    public DynamicReport reportClCertificaciones(ReportesController reportes) throws ClassNotFoundException {
        reportesController = reportes;


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.BOTTOM);

        Style detailStyle1 = new Style();
        detailStyle1.setVerticalAlign(VerticalAlign.BOTTOM);
        detailStyle1.setHorizontalAlign(HorizontalAlign.CENTER);


        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerStyle1 = new Style();
        headerStyle1.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle1.setBorderBottom(Border.PEN_1_POINT());
        headerStyle1.setBorderTop(Border.PEN_1_POINT());
        headerStyle1.setBackgroundColor(Color.white);
        headerStyle1.setTextColor(Color.DARK_GRAY);
        headerStyle1.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle1.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle1.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = (20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(17).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                //.setGrandTotalLegendStyle(headerVariables)
                //.setGrandTotalLegend("Costo Total de Recursos ")
                .setTemplateFile("templete/report8LetterCLcertificacion.jrxml");


        AbstractColumn id = ColumnBuilder.getNew()
                .setColumnProperty("empresa", Integer.class.getName())
                .setTitle("ID: ").setWidth(5)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("recurso", String.class.getName())
                .setTitle("Codigo").setWidth(18)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(70)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("U/M").setWidth(8)
                .setStyle(detailStyle1).setHeaderStyle(headerStyle1)
                .build();

        AbstractColumn columnapresup = ColumnBuilder.getNew()
                .setColumnProperty("presupuesto", Double.class.getName())
                .setTitle("Cantidad").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle1)
                .build();

        AbstractColumn columnacantidad = ColumnBuilder.getNew()
                .setColumnProperty("certificacion", Double.class.getName())
                .setTitle("Certificado").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle1)
                .build();

        AbstractColumn columnadespacho = ColumnBuilder.getNew()
                .setColumnProperty("despachos", Double.class.getName())
                .setTitle("Despachos").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle1)
                .build();

        AbstractColumn columnadiferencia = ColumnBuilder.getNew()
                .setColumnProperty("diferenciaCert", Double.class.getName())
                .setTitle("Disponible/Certificación").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle1)
                .build();

        AbstractColumn columnadiferencia1 = ColumnBuilder.getNew()
                .setColumnProperty("diferenciaDes", Double.class.getName())
                .setTitle("Disponible/C. Límite").setWidth(15).setPattern("0.0000")
                .setStyle(detailStyle1).setHeaderStyle(headerStyle1)
                .build();


        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();
        GroupBuilder gb4 = new GroupBuilder();

        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        DJGroup g5 = gb4.setCriteriaColumn((PropertyColumn) columngruoesp)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addGroup(g1);
        drb.addGroup(g2);
        drb.addGroup(g3);
        drb.addGroup(g5);
        drb.addColumn(columngruopEmpresa);
        drb.addColumn(columngruopZona);
        drb.addColumn(columngruopObj);
        drb.addColumn(columngruoesp);
        drb.addColumn(columnacode);
        drb.addColumn(columnadescrip);
        drb.addColumn(columnaum);
        drb.addColumn(columnapresup);
        drb.addColumn(columnacantidad);
        drb.addColumn(columnadespacho);
        drb.addColumn(columnadiferencia);
        drb.addColumn(columnadiferencia1);

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }


}

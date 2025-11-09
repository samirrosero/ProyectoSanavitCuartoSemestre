package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelReportes extends JPanel {

    private final PacienteDao pacienteDAO = new PacienteDao();
    private final MedicoDao medicoDAO = new MedicoDao();
    private final CitaDao citaDAO = new CitaDao();
    private final HistoriaClinicaDao historiaDAO = new HistoriaClinicaDao();

    public PanelReportes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(233, 247, 239)); // Fondo general verde menta claro
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === Título ===
        JLabel lblTitulo = new JLabel("📊 Panel de Reportes - Sanavit", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(46, 64, 46)); // Verde oscuro elegante
        add(lblTitulo, BorderLayout.NORTH);

        // === Panel Superior (Tarjetas) ===
        JPanel panelContadores = new JPanel(new GridLayout(1, 4, 20, 10));
        panelContadores.setBackground(new Color(233, 247, 239));

        int totalPacientes = pacienteDAO.obtenerTodosLosPacientes().size();
        int totalMedicos = MedicoDao.listarMedicos().size();
        int totalCitas = citaDAO.selectCita().size();
        int totalHistorias = historiaDAO.listarHistorias().size();

        // Todas en gama verde pastel, con leves diferencias de tono
        panelContadores.add(crearCard("👥 Pacientes", totalPacientes, new Color(168, 230, 207), new Color(184, 226, 200)));
        panelContadores.add(crearCard("🩺 Médicos", totalMedicos, new Color(177, 235, 210), new Color(200, 240, 215)));
        panelContadores.add(crearCard("📅 Citas", totalCitas, new Color(152, 223, 183), new Color(190, 238, 204)));
        panelContadores.add(crearCard("📄 Historias Clínicas", totalHistorias, new Color(139, 214, 177), new Color(190, 238, 204)));

        add(panelContadores, BorderLayout.CENTER);

        // === Panel de Gráficos ===
        JPanel panelGraficos = new JPanel(new GridLayout(1, 2, 20, 10));
        panelGraficos.setBackground(new Color(233, 247, 239));

        // === Gráfico Pastel: Modalidad de Citas ===
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        long presenciales = citaDAO.selectCita().stream().filter(c -> c.getIdModalidad() == 1).count();
        long virtuales = citaDAO.selectCita().stream().filter(c -> c.getIdModalidad() == 2).count();

        pieDataset.setValue("Presencial", presenciales);
        pieDataset.setValue("Virtual", virtuales);

        JFreeChart chartModalidad = ChartFactory.createPieChart(
                "Modalidad de Citas",
                pieDataset,
                true, true, false);

        PiePlot piePlot = (PiePlot) chartModalidad.getPlot();
        piePlot.setSectionPaint("Presencial", new Color(75, 214, 128)); // Verde pastel medio
        piePlot.setSectionPaint("Virtual", new Color(184, 226, 200));   // Verde claro
        piePlot.setBackgroundPaint(new Color(233, 247, 239));
        piePlot.setOutlineVisible(false);
        chartModalidad.setBackgroundPaint(new Color(233, 247, 239));
        chartModalidad.getTitle().setPaint(new Color(46, 64, 46));

        ChartPanel chartPanel1 = new ChartPanel(chartModalidad);
        panelGraficos.add(chartPanel1);

        // === Gráfico Barras: Citas por Mes ===
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Cita> citas = citaDAO.selectCita();
        int[] meses = new int[12];
        for (Cita c : citas) {
            if (c.getFechaCita() != null) {
                int mes = c.getFechaCita().toLocalDate().getMonthValue();
                meses[mes - 1]++;
            }
        }

        for (int i = 0; i < 12; i++) {
            dataset.addValue(meses[i], "Citas", obtenerNombreMes(i + 1));
        }

        JFreeChart chartBarras = ChartFactory.createBarChart(
                "Citas por Mes",
                "Mes",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        CategoryPlot plot = (CategoryPlot) chartBarras.getPlot();
        plot.setBackgroundPaint(new Color(233, 247, 239));
        plot.setRangeGridlinePaint(new Color(190, 210, 190));
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(152, 223, 183)); // Verde pastel medio
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);

        chartBarras.setBackgroundPaint(new Color(233, 247, 239));
        chartBarras.getTitle().setPaint(new Color(46, 64, 46));

        ChartPanel chartPanel2 = new ChartPanel(chartBarras);
        panelGraficos.add(chartPanel2);

        add(panelGraficos, BorderLayout.SOUTH);
    }

    // === Tarjeta con degradado verde pastel ===
    private JPanel crearCard(String titulo, int valor, Color color1, Color color2) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(46, 64, 46));

        JLabel lblValor = new JLabel(String.valueOf(valor), SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValor.setForeground(new Color(30, 60, 30));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    private String obtenerNombreMes(int mes) {
        String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        return meses[mes - 1];
    }
}

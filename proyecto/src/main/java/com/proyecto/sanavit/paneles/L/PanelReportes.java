package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelReportes extends JPanel {

    private PacienteDao pacienteDAO = new PacienteDao();
    private MedicoDao medicoDAO = new MedicoDao();
    private CitaDao citaDAO = new CitaDao();
    private HistoriaClinicaDao historiaDAO = new HistoriaClinicaDao();

    public PanelReportes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === Título ===
        JLabel lblTitulo = new JLabel("📊 Panel de Reportes - Sanavit", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 150, 243));
        add(lblTitulo, BorderLayout.NORTH);

        // === Panel Superior: Contadores ===
        JPanel panelContadores = new JPanel(new GridLayout(1, 4, 20, 10));
        panelContadores.setBackground(Color.WHITE);

        int totalPacientes = pacienteDAO.obtenerTodosLosPacientes().size();
        int totalMedicos = MedicoDao.listarMedicos().size();
        int totalCitas = citaDAO.selectCita().size();
        int totalHistorias = historiaDAO.listarHistorias().size();

        panelContadores.add(crearCard("👥 Pacientes", totalPacientes, new Color(76, 175, 80)));
        panelContadores.add(crearCard("🩺 Médicos", totalMedicos, new Color(33, 150, 243)));
        panelContadores.add(crearCard("📅 Citas", totalCitas, new Color(255, 152, 0)));
        panelContadores.add(crearCard("📄 Historias Clínicas", totalHistorias, new Color(244, 67, 54)));

        add(panelContadores, BorderLayout.CENTER);

        // === Panel Inferior: Gráficos ===
        JPanel panelGraficos = new JPanel(new GridLayout(1, 2, 20, 10));
        panelGraficos.setBackground(Color.WHITE);

        // === Gráfico 1: Modalidad ===
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        long presenciales = citaDAO.selectCita().stream().filter(c -> c.getIdModalidad() == 1).count();
        long virtuales = citaDAO.selectCita().stream().filter(c -> c.getIdModalidad() == 2).count();

        pieDataset.setValue("Presencial", presenciales);
        pieDataset.setValue("Virtual", virtuales);

        JFreeChart chartModalidad = ChartFactory.createPieChart(
                "Modalidad de Citas",
                pieDataset,
                true, true, false);

        ChartPanel chartPanel1 = new ChartPanel(chartModalidad);
        panelGraficos.add(chartPanel1);

        // === Gráfico 2: Citas por mes ===
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

        ChartPanel chartPanel2 = new ChartPanel(chartBarras);
        panelGraficos.add(chartPanel2);

        add(panelGraficos, BorderLayout.SOUTH);
    }

    // === Método auxiliar para crear tarjetas ===
    private JPanel crearCard(String titulo, int valor, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblValor = new JLabel(String.valueOf(valor), SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValor.setForeground(Color.WHITE);

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

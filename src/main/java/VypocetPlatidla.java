import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VypocetPlatidla extends JFrame {

    private JTextField zadanaCastkaField;
    private JTable vysledkyTable;
    private DefaultTableModel tableModel;
    private List<Platidlo> platidla;

    public VypocetPlatidla() {
        setTitle("Vypočet minimálního počtu platidel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicializace platidel
        platidla = new ArrayList<>();
        // Mince
        platidla.add(new Platidlo("mince", 1));
        platidla.add(new Platidlo("mince", 2));
        platidla.add(new Platidlo("mince", 5));
        platidla.add(new Platidlo("mince", 10));
        platidla.add(new Platidlo("mince", 20));
        platidla.add(new Platidlo("mince", 50));
        // Bankovky
        platidla.add(new Platidlo("bankovka", 100));
        platidla.add(new Platidlo("bankovka", 200));
        platidla.add(new Platidlo("bankovka", 500));
        platidla.add(new Platidlo("bankovka", 1000));
        platidla.add(new Platidlo("bankovka", 2000));
        platidla.add(new Platidlo("bankovka", 5000));

        // Vstupní panel
        JPanel vstupPanel = new JPanel();
        vstupPanel.add(new JLabel("Zadejte částku v Kč:"));
        zadanaCastkaField = new JTextField(10);
        vstupPanel.add(zadanaCastkaField);
        JButton vypocetButton = new JButton("Vypočítat");
        vypocetButton.addActionListener(new VypocetButtonListener());
        vstupPanel.add(vypocetButton);

        // Výčetka tlačítko
        JButton vycetkaButton = new JButton("Výčetka");
        vycetkaButton.addActionListener(new VycetkaButtonListener());
        vstupPanel.add(vycetkaButton);

        add(vstupPanel, BorderLayout.NORTH);

        // Výsledková tabulka
        tableModel = new DefaultTableModel(new Object[]{"Typ platidla", "Nominální hodnota", "Počet kusů"}, 0);
        vysledkyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vysledkyTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void vypocetPlatidla(int castka) {
        // Reset počtů
        for (Platidlo platidlo : platidla) {
            platidlo.setPocet(0);
        }

        // Výpočet minimálního počtu platidel
        for (int i = platidla.size() - 1; i >= 0; i--) {
            Platidlo platidlo = platidla.get(i);
            int pocet = castka / platidlo.getHodnota();
            platidlo.setPocet(pocet);
            castka %= platidlo.getHodnota();
        }

        // Aktualizace tabulky
        tableModel.setRowCount(0);
        for (Platidlo platidlo : platidla) {
            if (platidlo.getPocet() > 0) {
                tableModel.addRow(new Object[]{platidlo.getTyp(), platidlo.getHodnota(), platidlo.getPocet()});
            }
        }
    }

    private void vytvoritVycetku() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vycetka.txt"))) {
            for (Platidlo platidlo : platidla) {
                if (platidlo.getPocet() > 0) {
                    writer.write(platidlo.getTyp() + " " + platidlo.getHodnota() + " Kč: " + platidlo.getPocet() + " ks");
                    writer.newLine();
                }
            }
            JOptionPane.showMessageDialog(this, "Výčetka byla úspěšně vytvořena.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Chyba při vytváření výčetky.", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class VypocetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int castka = Integer.parseInt(zadanaCastkaField.getText());
                if (castka < 0) {
                    JOptionPane.showMessageDialog(VypocetPlatidla.this, "Zadejte prosím kladnou částku.", "Chyba", JOptionPane.ERROR_MESSAGE);
                } else {
                    vypocetPlatidla(castka);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(VypocetPlatidla.this, "Zadejte prosím platnou číselnou hodnotu.", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class VycetkaButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            vytvoritVycetku();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VypocetPlatidla frame = new VypocetPlatidla();
            frame.setVisible(true);
        });
    }
}
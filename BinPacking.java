import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class BinPacking extends JFrame {
    private double binCapacity = 1.0; // Capacidad del bin en kilogramos
    private ArrayList<Double> items; // Usamos Double para representar pesos en kilogramos
    private ArrayList<ArrayList<Double>> bins;
    private JTextField itemInputField;
    private JTextField binCapacityField; // Campo para ingresar la capacidad máxima del bin
    private JPanel binPanel;

    public BinPacking() {
        items = new ArrayList<>();
        bins = new ArrayList<>();
        setupUI();
    }

    private void firstFitAlgorithm() {
        bins.clear();
        // Ordenamos los elementos en orden decreciente
        Collections.sort(items, Collections.reverseOrder());

        for (double item : items) {
            boolean placed = false;
            for (ArrayList<Double> bin : bins) {
                double currentSum = bin.stream().mapToDouble(Double::doubleValue).sum();
                if (currentSum + item <= binCapacity) {
                    bin.add(item);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                ArrayList<Double> newBin = new ArrayList<>();
                newBin.add(item);
                bins.add(newBin);
            }
        }
    }

    private void setupUI() {
        setTitle("PROBLEMA DE LLENADO DE CAJONES");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        itemInputField = new JTextField(10);
        binCapacityField = new JTextField(10); // Campo para la capacidad máxima del bin
        JButton addButton = new JButton("AÑADIR Item");
        addButton.addActionListener(e -> addItem());

        JButton runButton = new JButton("INICIAR ALGORITMO");
        runButton.addActionListener(e -> runAlgorithm());

        controlPanel.add(new JLabel("PESO DE ITEM (kg):"));
        controlPanel.add(itemInputField);
        controlPanel.add(new JLabel("CAPACIDAD BIN (kg):")); // Etiqueta para la capacidad máxima del bin
        controlPanel.add(binCapacityField); // Campo para ingresar la capacidad máxima del bin
        controlPanel.add(addButton);
        controlPanel.add(runButton);

        binPanel = new JPanel();
        binPanel.setLayout(new BoxLayout(binPanel, BoxLayout.Y_AXIS));

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(binPanel), BorderLayout.CENTER);
    }

    private void addItem() {
        try {
            double itemWeight = Double.parseDouble(itemInputField.getText());
            if (itemWeight > 0) {
                items.add(itemWeight);
                itemInputField.setText("");
                JOptionPane.showMessageDialog(this, "Item added: " + itemWeight + " kg");
                displayItems();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor ingresa un valor.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "entrada invalida.");
        }
    }

    private void runAlgorithm() {
        try {
            binCapacity = Double.parseDouble(binCapacityField.getText()); // Actualizar la capacidad del bin
            firstFitAlgorithm();
            displayBins();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid bin capacity. Please enter a valid number.");
        }
    }

    private void displayItems() {
        binPanel.removeAll();
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new FlowLayout());
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Items"));

        for (double item : items) {
            JLabel itemLabel = new JLabel(String.valueOf(item));
            itemLabel.setPreferredSize(new Dimension(50, 30));
            itemLabel.setOpaque(true);
            itemLabel.setBackground(Color.CYAN);
            itemLabel.setHorizontalAlignment(SwingConstants.CENTER);

            itemLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JComponent comp = (JComponent) e.getSource();
                    TransferHandler handler = comp.getTransferHandler();
                    handler.exportAsDrag(comp, e, TransferHandler.COPY);
                }
            });

            itemsPanel.add(itemLabel);
        }

        binPanel.add(itemsPanel);
        binPanel.revalidate();
        binPanel.repaint();
    }

    private void displayBins() {
        binPanel.removeAll();
        for (ArrayList<Double> bin : bins) {
            JPanel binContainer = new JPanel();
            binContainer.setLayout(new FlowLayout());
            binContainer.setBorder(BorderFactory.createTitledBorder("Bin"));

            for (double item : bin) {
                JLabel itemLabel = new JLabel(String.valueOf(item));
                itemLabel.setPreferredSize(new Dimension(50, 30));
                itemLabel.setOpaque(true);
                itemLabel.setBackground(Color.CYAN);
                itemLabel.setHorizontalAlignment(SwingConstants.CENTER);
                binContainer.add(itemLabel);
            }

            binPanel.add(binContainer);
        }
        binPanel.revalidate();
        binPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BinPacking bp = new BinPacking();
            bp.setVisible(true);
        });
    }
}


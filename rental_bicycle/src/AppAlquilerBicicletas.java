import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

// Clases de servicio y utilidades
class ConexionBD {
    private static Connection conexion;
    private static final String URL = "jdbc:postgresql://localhost:5432/bicycle_rental";
    private static final String USER = "developer";
    private static final String PASSWORD = "23100132";

    public static void conectar() {
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada con éxito.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conectar(); // intenta reconectar si está cerrada
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la conexión: " + e.getMessage());
        }
        return conexion;
    }

}

// App principal
public class AppAlquilerBicicletas {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JPanel sidebarPanel;
    private static JPanel headerPanel;
    private static CardLayout cardLayout;
    private static JPanel contentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppAlquilerBicicletas::crearYMostrarGUI);
    }

    private static void crearYMostrarGUI() {
        frame = new JFrame("Sistema de Alquiler de Bicicletas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        sidebarPanel = new JPanel(new GridLayout(0, 1));
        sidebarPanel.setPreferredSize(new Dimension(200, 720));
        sidebarPanel.setBackground(new Color(50, 50, 50));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

        // Botones del sidebar
        JButton btnInicio = crearBotonSidebar("Inicio");
        JButton btnTiendas = crearBotonSidebar("Tiendas");
        JButton btnBicicletas = crearBotonSidebar("Bicicletas");
        JButton btnClientes = crearBotonSidebar("Clientes");
        JButton btnAlquileres = crearBotonSidebar("Alquileres");
        JButton btnPagos = crearBotonSidebar("Pagos");
        JButton btnProcesos = crearBotonSidebar("Procesos de Negocio");
        JButton btnReportes = crearBotonSidebar("Reportes");

        // Añadir botones al sidebar
        sidebarPanel.add(btnInicio);
        sidebarPanel.add(btnTiendas);
        sidebarPanel.add(btnBicicletas);
        sidebarPanel.add(btnClientes);
        sidebarPanel.add(btnAlquileres);
        sidebarPanel.add(btnPagos);
        sidebarPanel.add(btnProcesos);
        sidebarPanel.add(btnReportes);

        // Header
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1280, 50));
        headerPanel.setBackground(new Color(80, 80, 80));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JLabel lblTitulo = new JLabel("Bicycle Rental System", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCerrarSesion = new JButton("Salir de la Aplicación");
        btnCerrarSesion.setBackground(Color.RED);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "¿Seguro que quieres salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
            }
        });

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(btnCerrarSesion, BorderLayout.EAST);

        // Panel central
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Aquí debes agregar tus paneles específicos
        contentPanel.add(new PanelInicio(), "Inicio");
        contentPanel.add(new PanelTiendas(), "Tiendas");
        contentPanel.add(new PanelBicicletas(), "Bicicletas");
        contentPanel.add(new PanelClientes(), "Clientes");
        contentPanel.add(new PanelAlquileres(), "Alquileres");
        contentPanel.add(new PanelPagos(), "Pagos");
        contentPanel.add(new PanelProcesos(), "Procesos");
        contentPanel.add(new PanelReportes(), "Reportes");

        // Acciones de botones
        btnInicio.addActionListener(e -> cardLayout.show(contentPanel, "Inicio"));
        btnTiendas.addActionListener(e -> cardLayout.show(contentPanel, "Tiendas"));
        btnBicicletas.addActionListener(e -> cardLayout.show(contentPanel, "Bicicletas"));
        btnClientes.addActionListener(e -> cardLayout.show(contentPanel, "Clientes"));
        btnAlquileres.addActionListener(e -> cardLayout.show(contentPanel, "Alquileres"));
        btnPagos.addActionListener(e -> cardLayout.show(contentPanel, "Pagos"));
        btnProcesos.addActionListener(e -> cardLayout.show(contentPanel, "Procesos"));
        btnReportes.addActionListener(e -> cardLayout.show(contentPanel, "Reportes"));

        // Estructura final
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private static JButton crearBotonSidebar(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(70, 70, 70));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.PLAIN, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(100, 100, 100));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 70, 70));
            }
        });
        return boton;
    }

    static class PanelInicio extends JPanel {
        public PanelInicio() {
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(245, 245, 245)); // Gris claro neutro
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // Panel superior con información
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(new Color(245, 245, 245));
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                            "Bienvenido a BikeRent Manager"),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            JTextArea infoArea = new JTextArea();
            infoArea.setText("Sistema Integral de Gestión para Alquiler de Bicicletas\n\n"
                    + "Esta aplicación le permite administrar todo el ciclo de alquiler:\n"
                    + "- Control de inventario de bicicletas por tienda\n"
                    + "- Gestión de clientes y reservas\n"
                    + "- Seguimiento de alquileres activos y devoluciones\n"
                    + "- Generación de reportes de rentabilidad y uso\n\n"
                    + "Nuestro compromiso: Ofrecer la mejor plataforma para gestionar tu negocio de alquiler de bicicletas.");
            infoArea.setEditable(false);
            infoArea.setMargin(new Insets(10, 15, 10, 15));
            infoArea.setLineWrap(true);
            infoArea.setWrapStyleWord(true);
            infoArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
            infoArea.setBackground(new Color(250, 250, 250)); // Gris muy claro
            infoArea.setForeground(new Color(70, 70, 70)); // Gris oscuro

            infoPanel.add(infoArea, BorderLayout.CENTER);
            add(infoPanel, BorderLayout.NORTH);

            // Panel central con imágenes y botones
            JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
            centerPanel.setBackground(new Color(245, 245, 245));

            // Panel izquierdo para imágenes de bicicletas
            JPanel imagePanel = new JPanel(new GridLayout(2, 1, 10, 10));
            imagePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100)),
                    "Nuestras Bicicletas"));
            imagePanel.setBackground(new Color(245, 245, 245));

            // Imagen 1 - Bicicleta urbana
            JLabel imgBike1;
            try {
                ImageIcon bikeIcon1 = new ImageIcon(getClass().getResource("/images/bike1.jpg"));
                // Redimensionar la imagen para que se ajuste al espacio
                Image img = bikeIcon1.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                imgBike1 = new JLabel(new ImageIcon(img));
                imgBike1.setToolTipText("Bicicletas urbanas para alquiler");
            } catch (NullPointerException e) {
                imgBike1 = new JLabel("Imagen no disponible", JLabel.CENTER);
                imgBike1.setForeground(Color.RED);
            }
            imgBike1.setHorizontalAlignment(JLabel.CENTER);
            imgBike1.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150), 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            imgBike1.setBackground(Color.WHITE);
            imgBike1.setOpaque(true);

            // Imagen 2 - Bicicleta de montaña
            JLabel imgBike2;
            try {
                ImageIcon bikeIcon2 = new ImageIcon(getClass().getResource("/images/bike2.jpg"));
                // Redimensionar la imagen
                Image img2 = bikeIcon2.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                imgBike2 = new JLabel(new ImageIcon(img2));
                imgBike2.setToolTipText("Bicicletas de montaña para aventuras");
            } catch (NullPointerException e) {
                imgBike2 = new JLabel("Imagen no disponible", JLabel.CENTER);
                imgBike2.setForeground(Color.RED);
            }
            imgBike2.setHorizontalAlignment(JLabel.CENTER);
            imgBike2.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150), 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            imgBike2.setBackground(Color.WHITE);
            imgBike2.setOpaque(true);

            imagePanel.add(imgBike1);
            imagePanel.add(imgBike2);
            centerPanel.add(imagePanel);

            // Panel de botones con estilo neutro
            JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 15));
            buttonPanel.setBackground(new Color(245, 245, 245));
            buttonPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100)),
                    "Acciones Rápidas"));

            // Botón para realizar copias de seguridad (usando DatabaseUtils)
            JButton backupButton = createNeutralButton("📁 Realizar Respaldo",
                    new Color(120, 120, 120), // Gris medio
                    e -> utils.DatabaseUtils.realizarCopiasDeSeguridad(this));

            // Botón para restaurar la base de datos (usando DatabaseUtils)
            JButton restoreButton = createNeutralButton("🔄 Restaurar Datos",
                    new Color(90, 90, 90), // Gris oscuro
                    e -> utils.DatabaseUtils.restaurarBaseDeDatos(this));

            // Botón para gestión de inventario
            JButton inventoryButton = createNeutralButton("🚲 Inventario Bicicletas",
                    new Color(150, 150, 150), // Gris claro
                    e -> JOptionPane.showMessageDialog(this,
                            "Módulo de inventario de bicicletas por tienda",
                            "Gestión de Inventario",
                            JOptionPane.INFORMATION_MESSAGE));

            buttonPanel.add(backupButton);
            buttonPanel.add(restoreButton);
            buttonPanel.add(inventoryButton);
            centerPanel.add(buttonPanel);

            add(centerPanel, BorderLayout.CENTER);

            // Panel inferior con estadísticas
            JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            statsPanel.setBackground(new Color(245, 245, 245));
            statsPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100)),
                    "Estadísticas del Negocio"));

            // Método para crear labels de estadísticas con estilo
            statsPanel.add(createStatLabel("Bicicletas Disponibles", "42"));
            statsPanel.add(createStatLabel("Tiendas Activas", "5"));
            statsPanel.add(createStatLabel("Alquileres Hoy", "18"));

            add(statsPanel, BorderLayout.SOUTH);
        }

        // Método auxiliar para crear botones con estilo neutro
        private JButton createNeutralButton(String text, Color bgColor, ActionListener action) {
            JButton button = new JButton(text);
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)), // Gris claro
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));
            button.addActionListener(action);
            return button;
        }

        // Método auxiliar para crear labels de estadísticas
        private JPanel createStatLabel(String title, String value) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(250, 250, 250));
            panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

            JLabel titleLabel = new JLabel(title, JLabel.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            titleLabel.setForeground(new Color(70, 70, 70));

            JLabel valueLabel = new JLabel(value, JLabel.CENTER);
            valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            valueLabel.setForeground(new Color(50, 50, 50));

            panel.add(titleLabel, BorderLayout.NORTH);
            panel.add(valueLabel, BorderLayout.CENTER);

            return panel;
        }
    }

    static class PanelTiendas extends JPanel {
        private JTable tiendasTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelTiendas() {
            setLayout(new BorderLayout());

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Tiendas", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.BLACK));
            panelTabla.setBackground(new Color(230, 230, 250));

            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Contacto");
            tableModel.addColumn("Localización");
            tableModel.addColumn("Email");
            tableModel.addColumn("Teléfono");
            tableModel.addColumn("Dirección");
            tableModel.addColumn("Otros Detalles");

            tiendasTable = new JTable(tableModel);
            tiendasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tiendasTable.setFont(new Font("Arial", Font.PLAIN, 12));
            tiendasTable.setRowHeight(25);
            tiendasTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            TableColumnModel columnModel = tiendasTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(10);  // ID
            columnModel.getColumn(1).setPreferredWidth(150); // Contacto
            columnModel.getColumn(2).setPreferredWidth(150); // Localización
            columnModel.getColumn(3).setPreferredWidth(150); // Email
            columnModel.getColumn(4).setPreferredWidth(100); // Teléfono
            columnModel.getColumn(5).setPreferredWidth(200); // Dirección
            columnModel.getColumn(6).setPreferredWidth(200); // Otros

            tiendasTable.setBackground(Color.WHITE);
            tiendasTable.setGridColor(new Color(200, 200, 200));
            tiendasTable.setSelectionBackground(new Color(173, 216, 230));
            tiendasTable.setSelectionForeground(Color.BLACK);

            tiendasTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    return c;
                }
            });

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            tiendasTable.setRowSorter(sorter);

            JScrollPane scrollPane = new JScrollPane(tiendasTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            // Panel agregar/modificar
            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Tienda", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelAgregar.setBackground(new Color(240, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField contactoField = new JTextField(15);
            JTextField localizacionField = new JTextField(15);
            JTextField emailField = new JTextField(15);
            JTextField telefonoField = new JTextField(15);
            JTextField direccionField = new JTextField(15);
            JTextField otrosField = new JTextField(15);

            // Etiquetas y campos
            gbc.gridx = 0;
            gbc.gridy = 0;
            panelAgregar.add(new JLabel("Contacto*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(contactoField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            panelAgregar.add(new JLabel("Localización*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(localizacionField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            panelAgregar.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(emailField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            panelAgregar.add(new JLabel("Teléfono:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(telefonoField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            panelAgregar.add(new JLabel("Dirección*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(direccionField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 5;
            panelAgregar.add(new JLabel("Otros Detalles:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(otrosField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            panelAgregar.add(new JLabel("* Campo obligatorio"), gbc);

            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton agregarButton = new JButton("Agregar");
            JButton modificarButton = new JButton("Modificar");
            JButton eliminarButton = new JButton("Eliminar");

            agregarButton.setBackground(new Color(70, 130, 180));
            modificarButton.setBackground(new Color(34, 139, 34));
            eliminarButton.setBackground(new Color(255, 69, 0));
            agregarButton.setForeground(Color.WHITE);
            modificarButton.setForeground(Color.WHITE);
            eliminarButton.setForeground(Color.WHITE);

            modificarButton.setEnabled(false);
            eliminarButton.setEnabled(false);

            tiendasTable.getSelectionModel().addListSelectionListener(e -> {
                boolean seleccion = tiendasTable.getSelectedRow() != -1;
                modificarButton.setEnabled(seleccion);
                eliminarButton.setEnabled(seleccion);
            });

            agregarButton.addActionListener(e -> {
                if (contactoField.getText().isEmpty() || localizacionField.getText().isEmpty()
                        || direccionField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos marcados con * son obligatorios.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                agregarTienda(
                        contactoField.getText(), localizacionField.getText(), emailField.getText(),
                        telefonoField.getText(), direccionField.getText(), otrosField.getText()
                );
            });

            modificarButton.addActionListener(e -> {
                int selectedRow = tiendasTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    modificarTienda(id,
                            contactoField.getText(), localizacionField.getText(), emailField.getText(),
                            telefonoField.getText(), direccionField.getText(), otrosField.getText()
                    );
                }
            });

            eliminarButton.addActionListener(e -> {
                int selectedRow = tiendasTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarTienda(id);
                }
            });

            panelBotones.add(agregarButton);
            panelBotones.add(modificarButton);
            panelBotones.add(eliminarButton);
            gbc.gridy = 7;
            panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarTiendas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarTiendas() {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Multi_Shop");
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("Multi_Shop_ID"),
                            rs.getString("Contact_Name"),
                            rs.getString("Location_Name"),
                            rs.getString("Email_Address"),
                            rs.getString("Phone_Number"),
                            rs.getString("Address"),
                            rs.getString("Other_Details")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar tiendas desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void agregarTienda(String contacto, String localizacion, String email, String telefono, String direccion, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Multi_Shop (Contact_Name, Location_Name, Email_Address, Phone_Number, Address, Other_Details) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, contacto);
                ps.setString(2, localizacion);
                ps.setString(3, email);
                ps.setString(4, telefono);
                ps.setString(5, direccion);
                ps.setString(6, otros);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Tienda agregada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarTiendas();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al agregar la tienda.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void modificarTienda(int id, String contacto, String localizacion, String email, String telefono, String direccion, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE Multi_Shop SET Contact_Name=?, Location_Name=?, Email_Address=?, Phone_Number=?, Address=?, Other_Details=? WHERE Multi_Shop_ID=?");
                ps.setString(1, contacto);
                ps.setString(2, localizacion);
                ps.setString(3, email);
                ps.setString(4, telefono);
                ps.setString(5, direccion);
                ps.setString(6, otros);
                ps.setInt(7, id);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Tienda modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarTiendas();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al modificar la tienda.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void eliminarTienda(int id) {
            try {
                PreparedStatement ps = connection.prepareStatement("DELETE FROM Multi_Shop WHERE Multi_Shop_ID = ?");
                ps.setInt(1, id);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Tienda eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarTiendas();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar la tienda.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class PanelBicicletas extends JPanel {
        private JTable bicicletasTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelBicicletas() {
            setLayout(new BorderLayout());

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Bicicletas en Tiendas", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.BLACK));
            panelTabla.setBackground(new Color(230, 230, 250));

            tableModel = new DefaultTableModel();
            tableModel.addColumn("Tienda ID");
            tableModel.addColumn("Bicicleta ID");
            tableModel.addColumn("Fecha Entrada");
            tableModel.addColumn("Fecha Salida");
            tableModel.addColumn("Otros Detalles");

            bicicletasTable = new JTable(tableModel);
            bicicletasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bicicletasTable.setFont(new Font("Arial", Font.PLAIN, 12));
            bicicletasTable.setRowHeight(25);
            bicicletasTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            TableColumnModel columnModel = bicicletasTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(1).setPreferredWidth(50);
            columnModel.getColumn(2).setPreferredWidth(120);
            columnModel.getColumn(3).setPreferredWidth(120);
            columnModel.getColumn(4).setPreferredWidth(200);

            bicicletasTable.setBackground(Color.WHITE);
            bicicletasTable.setGridColor(new Color(200, 200, 200));
            bicicletasTable.setSelectionBackground(new Color(173, 216, 230));
            bicicletasTable.setSelectionForeground(Color.BLACK);

            bicicletasTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    return c;
                }
            });

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            bicicletasTable.setRowSorter(sorter);

            JScrollPane scrollPane = new JScrollPane(bicicletasTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            // Panel agregar/modificar
            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Bicicleta en Tienda", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelAgregar.setBackground(new Color(240, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField tiendaIdField = new JTextField(10);
            JTextField bicicletaIdField = new JTextField(10);
            JTextField fechaOutField = new JTextField(15);
            JTextField otrosField = new JTextField(15);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panelAgregar.add(new JLabel("Tienda ID*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(tiendaIdField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            panelAgregar.add(new JLabel("Bicicleta ID*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(bicicletaIdField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            panelAgregar.add(new JLabel("Fecha Salida:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(fechaOutField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            panelAgregar.add(new JLabel("Otros Detalles:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(otrosField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            panelAgregar.add(new JLabel("* Campo obligatorio"), gbc);

            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton agregarButton = new JButton("Agregar");
            JButton modificarButton = new JButton("Modificar");
            JButton eliminarButton = new JButton("Eliminar");

            agregarButton.setBackground(new Color(70, 130, 180));
            modificarButton.setBackground(new Color(34, 139, 34));
            eliminarButton.setBackground(new Color(255, 69, 0));
            agregarButton.setForeground(Color.WHITE);
            modificarButton.setForeground(Color.WHITE);
            eliminarButton.setForeground(Color.WHITE);

            modificarButton.setEnabled(false);
            eliminarButton.setEnabled(false);

            bicicletasTable.getSelectionModel().addListSelectionListener(e -> {
                boolean seleccion = bicicletasTable.getSelectedRow() != -1;
                modificarButton.setEnabled(seleccion);
                eliminarButton.setEnabled(seleccion);
            });

            agregarButton.addActionListener(e -> {
                if (tiendaIdField.getText().isEmpty() || bicicletaIdField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Los campos obligatorios no pueden estar vacíos.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                agregarRegistro(
                        Integer.parseInt(tiendaIdField.getText()),
                        Integer.parseInt(bicicletaIdField.getText()),
                        fechaOutField.getText(),
                        otrosField.getText()
                );
            });

            modificarButton.addActionListener(e -> {
                int row = bicicletasTable.getSelectedRow();
                if (row != -1) {
                    int tiendaId = (int) tableModel.getValueAt(row, 0);
                    int biciId = (int) tableModel.getValueAt(row, 1);
                    Timestamp fechaIn = Timestamp.valueOf((String) tableModel.getValueAt(row, 2));
                    modificarRegistro(tiendaId, biciId, fechaIn,
                            fechaOutField.getText(), otrosField.getText());
                }
            });

            eliminarButton.addActionListener(e -> {
                int row = bicicletasTable.getSelectedRow();
                if (row != -1) {
                    int tiendaId = (int) tableModel.getValueAt(row, 0);
                    int biciId = (int) tableModel.getValueAt(row, 1);
                    Timestamp fechaIn = Timestamp.valueOf((String) tableModel.getValueAt(row, 2));
                    eliminarRegistro(tiendaId, biciId, fechaIn);
                }
            });

            panelBotones.add(agregarButton);
            panelBotones.add(modificarButton);
            panelBotones.add(eliminarButton);
            gbc.gridy = 5;
            panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarRegistros();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarRegistros() {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Bicycles_in_Shops");
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("Multi_Shop_ID"),
                            rs.getInt("Bicycle_ID"),
                            rs.getTimestamp("DateTime_In").toString(),
                            rs.getTimestamp("DateTime_Out") != null ? rs.getTimestamp("DateTime_Out").toString() : "",
                            rs.getString("Other_Details")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void agregarRegistro(int tiendaId, int biciId, String fechaOut, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Bicycles_in_Shops (Multi_Shop_ID, Bicycle_ID, DateTime_Out, Other_Details) VALUES (?, ?, ?, ?)");
                ps.setInt(1, tiendaId);
                ps.setInt(2, biciId);
                if (fechaOut == null || fechaOut.isEmpty()) {
                    ps.setNull(3, Types.TIMESTAMP);
                } else {
                    ps.setTimestamp(3, Timestamp.valueOf(fechaOut));
                }
                ps.setString(4, otros);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Registro agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarRegistros();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al agregar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void modificarRegistro(int tiendaId, int biciId, Timestamp fechaIn, String fechaOut, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE Bicycles_in_Shops SET DateTime_Out = ?, Other_Details = ? WHERE Multi_Shop_ID = ? AND Bicycle_ID = ? AND DateTime_In = ?");
                if (fechaOut == null || fechaOut.isEmpty()) {
                    ps.setNull(1, Types.TIMESTAMP);
                } else {
                    ps.setTimestamp(1, Timestamp.valueOf(fechaOut));
                }
                ps.setString(2, otros);
                ps.setInt(3, tiendaId);
                ps.setInt(4, biciId);
                ps.setTimestamp(5, fechaIn);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Registro modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarRegistros();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al modificar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void eliminarRegistro(int tiendaId, int biciId, Timestamp fechaIn) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "DELETE FROM Bicycles_in_Shops WHERE Multi_Shop_ID = ? AND Bicycle_ID = ? AND DateTime_In = ?");
                ps.setInt(1, tiendaId);
                ps.setInt(2, biciId);
                ps.setTimestamp(3, fechaIn);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarRegistros();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class PanelClientes extends JPanel {
        private JTable clientesTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelClientes() {
            setLayout(new BorderLayout());

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Clientes", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.BLACK));
            panelTabla.setBackground(new Color(230, 230, 250));

            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Registrado en");
            tableModel.addColumn("Último alquiler");
            tableModel.addColumn("Otros Detalles");

            clientesTable = new JTable(tableModel);
            clientesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            clientesTable.setFont(new Font("Arial", Font.PLAIN, 12));
            clientesTable.setRowHeight(25);
            clientesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            TableColumnModel columnModel = clientesTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(10);  // ID
            columnModel.getColumn(1).setPreferredWidth(150); // Fecha registro
            columnModel.getColumn(2).setPreferredWidth(150); // Último alquiler
            columnModel.getColumn(3).setPreferredWidth(200); // Otros detalles

            clientesTable.setBackground(Color.WHITE);
            clientesTable.setGridColor(new Color(200, 200, 200));
            clientesTable.setSelectionBackground(new Color(173, 216, 230));
            clientesTable.setSelectionForeground(Color.BLACK);

            clientesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    return c;
                }
            });

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            clientesTable.setRowSorter(sorter);

            JScrollPane scrollPane = new JScrollPane(clientesTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            // Panel agregar/modificar
            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Cliente", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelAgregar.setBackground(new Color(240, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField ultimoAlquilerField = new JTextField(15);
            JTextField otrosField = new JTextField(15);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panelAgregar.add(new JLabel("Último alquiler (yyyy-mm-dd hh:mm:ss):"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(ultimoAlquilerField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            panelAgregar.add(new JLabel("Otros Detalles:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(otrosField, gbc);

            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton agregarButton = new JButton("Agregar");
            JButton modificarButton = new JButton("Modificar");
            JButton eliminarButton = new JButton("Eliminar");

            agregarButton.setBackground(new Color(70, 130, 180));
            modificarButton.setBackground(new Color(34, 139, 34));
            eliminarButton.setBackground(new Color(255, 69, 0));
            agregarButton.setForeground(Color.WHITE);
            modificarButton.setForeground(Color.WHITE);
            eliminarButton.setForeground(Color.WHITE);

            modificarButton.setEnabled(false);
            eliminarButton.setEnabled(false);

            clientesTable.getSelectionModel().addListSelectionListener(e -> {
                boolean seleccion = clientesTable.getSelectedRow() != -1;
                modificarButton.setEnabled(seleccion);
                eliminarButton.setEnabled(seleccion);
            });

            agregarButton.addActionListener(e -> {
                agregarCliente(ultimoAlquilerField.getText(), otrosField.getText());
            });

            modificarButton.addActionListener(e -> {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    modificarCliente(id, ultimoAlquilerField.getText(), otrosField.getText());
                }
            });

            eliminarButton.addActionListener(e -> {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarCliente(id);
                }
            });

            panelBotones.add(agregarButton);
            panelBotones.add(modificarButton);
            panelBotones.add(eliminarButton);
            gbc.gridy = 2;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarClientes() {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Renters");
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("Renter_ID"),
                            rs.getTimestamp("Registration_Date_Time"),
                            rs.getTimestamp("Last_Rental_Date_Time"),
                            rs.getString("Other_Details")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar clientes desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void agregarCliente(String ultimoAlquiler, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Renters (Last_Rental_Date_Time, Other_Details) VALUES (?, ?)");
                if (ultimoAlquiler != null && !ultimoAlquiler.isBlank()) {
                    ps.setTimestamp(1, Timestamp.valueOf(ultimoAlquiler));
                } else {
                    ps.setNull(1, Types.TIMESTAMP);
                }
                ps.setString(2, otros);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarClientes();
                }
            } catch (SQLException | IllegalArgumentException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al agregar el cliente. Verifique el formato de la fecha.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void modificarCliente(int id, String ultimoAlquiler, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE Renters SET Last_Rental_Date_Time=?, Other_Details=? WHERE Renter_ID=?");
                if (ultimoAlquiler != null && !ultimoAlquiler.isBlank()) {
                    ps.setTimestamp(1, Timestamp.valueOf(ultimoAlquiler));
                } else {
                    ps.setNull(1, Types.TIMESTAMP);
                }
                ps.setString(2, otros);
                ps.setInt(3, id);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarClientes();
                }
            } catch (SQLException | IllegalArgumentException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al modificar el cliente. Verifique el formato de la fecha.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void eliminarCliente(int id) {
            try {
                PreparedStatement ps = connection.prepareStatement("DELETE FROM Renters WHERE Renter_ID = ?");
                ps.setInt(1, id);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarClientes();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class PanelAlquileres extends JPanel {
        private JTable alquileresTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelAlquileres() {
            setLayout(new BorderLayout());

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Alquileres", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.BLACK));
            panelTabla.setBackground(new Color(230, 230, 250));

            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Bicicleta");
            tableModel.addColumn("Estado Pago");
            tableModel.addColumn("Tarifa Alquiler");
            tableModel.addColumn("Inquilino");
            tableModel.addColumn("Método de Pago");
            tableModel.addColumn("Todo el Día");
            tableModel.addColumn("Fecha Reserva Inicio");
            tableModel.addColumn("Fecha Reserva Fin");
            tableModel.addColumn("Fecha Real Inicio");
            tableModel.addColumn("Fecha Real Fin");
            tableModel.addColumn("Pago Adeudado");
            tableModel.addColumn("Pago Realizado");
            tableModel.addColumn("Detalles");

            alquileresTable = new JTable(tableModel);
            alquileresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            alquileresTable.setFont(new Font("Arial", Font.PLAIN, 12));
            alquileresTable.setRowHeight(25);
            alquileresTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            TableColumnModel columnModel = alquileresTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(10);  // ID
            columnModel.getColumn(1).setPreferredWidth(100); // Bicicleta
            columnModel.getColumn(2).setPreferredWidth(100); // Estado Pago
            columnModel.getColumn(3).setPreferredWidth(100); // Tarifa Alquiler
            columnModel.getColumn(4).setPreferredWidth(100); // Inquilino
            columnModel.getColumn(5).setPreferredWidth(100); // Método de Pago
            columnModel.getColumn(6).setPreferredWidth(50);  // Todo el Día
            columnModel.getColumn(7).setPreferredWidth(150); // Fecha Reserva Inicio
            columnModel.getColumn(8).setPreferredWidth(150); // Fecha Reserva Fin
            columnModel.getColumn(9).setPreferredWidth(150); // Fecha Real Inicio
            columnModel.getColumn(10).setPreferredWidth(150); // Fecha Real Fin
            columnModel.getColumn(11).setPreferredWidth(100); // Pago Adeudado
            columnModel.getColumn(12).setPreferredWidth(100); // Pago Realizado
            columnModel.getColumn(13).setPreferredWidth(200); // Detalles

            alquileresTable.setBackground(Color.WHITE);
            alquileresTable.setGridColor(new Color(200, 200, 200));
            alquileresTable.setSelectionBackground(new Color(173, 216, 230));
            alquileresTable.setSelectionForeground(Color.BLACK);

            alquileresTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    return c;
                }
            });

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            alquileresTable.setRowSorter(sorter);

            JScrollPane scrollPane = new JScrollPane(alquileresTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            // Panel agregar/modificar
            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Alquiler", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelAgregar.setBackground(new Color(240, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField bicicletaField = new JTextField(15);
            JTextField estadoPagoField = new JTextField(15);
            JTextField tarifaAlquilerField = new JTextField(15);
            JTextField inquilinoField = new JTextField(15);
            JTextField metodoPagoField = new JTextField(15);
            JTextField todoDiaField = new JTextField(15);
            JTextField fechaReservaInicioField = new JTextField(15);
            JTextField fechaReservaFinField = new JTextField(15);
            JTextField fechaRealInicioField = new JTextField(15);
            JTextField fechaRealFinField = new JTextField(15);
            JTextField pagoAdeudadoField = new JTextField(15);
            JTextField pagoRealizadoField = new JTextField(15);
            JTextField otrosField = new JTextField(15);

            // Etiquetas y campos
            gbc.gridx = 0;
            gbc.gridy = 0;
            panelAgregar.add(new JLabel("Bicicleta*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(bicicletaField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            panelAgregar.add(new JLabel("Estado Pago*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(estadoPagoField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            panelAgregar.add(new JLabel("Tarifa Alquiler*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(tarifaAlquilerField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            panelAgregar.add(new JLabel("Inquilino*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(inquilinoField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            panelAgregar.add(new JLabel("Método de Pago*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(metodoPagoField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 5;
            panelAgregar.add(new JLabel("Todo el Día (Y/N):"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(todoDiaField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 6;
            panelAgregar.add(new JLabel("Fecha Reserva Inicio*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(fechaReservaInicioField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 7;
            panelAgregar.add(new JLabel("Fecha Reserva Fin*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(fechaReservaFinField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 8;
            panelAgregar.add(new JLabel("Fecha Real Inicio:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(fechaRealInicioField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 9;
            panelAgregar.add(new JLabel("Fecha Real Fin:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(fechaRealFinField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 10;
            panelAgregar.add(new JLabel("Pago Adeudado*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(pagoAdeudadoField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 11;
            panelAgregar.add(new JLabel("Pago Realizado:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(pagoRealizadoField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 12;
            panelAgregar.add(new JLabel("Otros Detalles:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(otrosField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 13;
            gbc.gridwidth = 2;
            panelAgregar.add(new JLabel("* Campo obligatorio"), gbc);

            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton agregarButton = new JButton("Agregar");
            JButton modificarButton = new JButton("Modificar");
            JButton eliminarButton = new JButton("Eliminar");

            agregarButton.setBackground(new Color(70, 130, 180));
            modificarButton.setBackground(new Color(34, 139, 34));
            eliminarButton.setBackground(new Color(255, 69, 0));
            agregarButton.setForeground(Color.WHITE);
            modificarButton.setForeground(Color.WHITE);
            eliminarButton.setForeground(Color.WHITE);

            modificarButton.setEnabled(false);
            eliminarButton.setEnabled(false);

            alquileresTable.getSelectionModel().addListSelectionListener(e -> {
                boolean seleccion = alquileresTable.getSelectedRow() != -1;
                modificarButton.setEnabled(seleccion);
                eliminarButton.setEnabled(seleccion);
            });

            agregarButton.addActionListener(e -> {
                if (bicicletaField.getText().isEmpty() || estadoPagoField.getText().isEmpty()
                        || tarifaAlquilerField.getText().isEmpty() || inquilinoField.getText().isEmpty()
                        || metodoPagoField.getText().isEmpty() || fechaReservaInicioField.getText().isEmpty()
                        || fechaReservaFinField.getText().isEmpty() || pagoAdeudadoField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos marcados con * son obligatorios.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                agregarAlquiler(
                        bicicletaField.getText(), estadoPagoField.getText(), tarifaAlquilerField.getText(),
                        inquilinoField.getText(), metodoPagoField.getText(), todoDiaField.getText(),
                        fechaReservaInicioField.getText(), fechaReservaFinField.getText(),
                        fechaRealInicioField.getText(), fechaRealFinField.getText(),
                        pagoAdeudadoField.getText(), pagoRealizadoField.getText(), otrosField.getText()
                );
            });

            modificarButton.addActionListener(e -> {
                int selectedRow = alquileresTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    modificarAlquiler(id,
                            bicicletaField.getText(), estadoPagoField.getText(), tarifaAlquilerField.getText(),
                            inquilinoField.getText(), metodoPagoField.getText(), todoDiaField.getText(),
                            fechaReservaInicioField.getText(), fechaReservaFinField.getText(),
                            fechaRealInicioField.getText(), fechaRealFinField.getText(),
                            pagoAdeudadoField.getText(), pagoRealizadoField.getText(), otrosField.getText()
                    );
                }
            });

            eliminarButton.addActionListener(e -> {
                int selectedRow = alquileresTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarAlquiler(id);
                }
            });

            panelBotones.add(agregarButton);
            panelBotones.add(modificarButton);
            panelBotones.add(eliminarButton);
            gbc.gridy = 14;
            panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarAlquileres();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarAlquileres() {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Rentals");
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("Rental_ID"),
                            rs.getInt("Bicycle_ID"),
                            rs.getString("Payment_status_Code"),
                            rs.getInt("Rental_Rates_ID"),
                            rs.getInt("Renter_ID"),
                            rs.getInt("Renter_Payment_Method_ID"),
                            rs.getString("All_Day_Rental_YN"),
                            rs.getTimestamp("Booked_Start_Date_Time"),
                            rs.getTimestamp("Booked_End_Date_Time"),
                            rs.getTimestamp("Actual_Start_Date_Time"),
                            rs.getTimestamp("Actual_End_Date_Time"),
                            rs.getBigDecimal("Rental_Payment_Due"),
                            rs.getBigDecimal("Rental_Payment_Made"),
                            rs.getString("Other_Details")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar alquileres desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void agregarAlquiler(String bicicleta, String estadoPago, String tarifaAlquiler, String inquilino,
                                     String metodoPago, String todoDia, String fechaReservaInicio, String fechaReservaFin,
                                     String fechaRealInicio, String fechaRealFin, String pagoAdeudado,
                                     String pagoRealizado, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Rentals (Bicycle_ID, Payment_status_Code, Rental_Rates_ID, Renter_ID, Renter_Payment_Method_ID, " +
                                "All_Day_Rental_YN, Booked_Start_Date_Time, Booked_End_Date_Time, Actual_Start_Date_Time, Actual_End_Date_Time, " +
                                "Rental_Payment_Due, Rental_Payment_Made, Other_Details) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                // Establecer parámetros según los campos
                // ...
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Alquiler agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarAlquileres();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al agregar el alquiler.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void modificarAlquiler(int id, String bicicleta, String estadoPago, String tarifaAlquiler, String inquilino,
                                       String metodoPago, String todoDia, String fechaReservaInicio, String fechaReservaFin,
                                       String fechaRealInicio, String fechaRealFin, String pagoAdeudado,
                                       String pagoRealizado, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE Rentals SET Bicycle_ID=?, Payment_status_Code=?, Rental_Rates_ID=?, Renter_ID=?, Renter_Payment_Method_ID=?, " +
                                "All_Day_Rental_YN=?, Booked_Start_Date_Time=?, Booked_End_Date_Time=?, Actual_Start_Date_Time=?, Actual_End_Date_Time=?, " +
                                "Rental_Payment_Due=?, Rental_Payment_Made=?, Other_Details=? WHERE Rental_ID=?");
                // Establecer parámetros según los campos
                // ...
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Alquiler modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarAlquileres();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al modificar el alquiler.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void eliminarAlquiler(int id) {
            try {
                PreparedStatement ps = connection.prepareStatement("DELETE FROM Rentals WHERE Rental_ID = ?");
                ps.setInt(1, id);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Alquiler eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarAlquileres();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar el alquiler.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class PanelPagos extends JPanel {
        private JTable pagosTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelPagos() {
            setLayout(new BorderLayout());

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Métodos de Pago", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.BLACK));
            panelTabla.setBackground(new Color(230, 230, 250));

            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Renter ID");
            tableModel.addColumn("Método de Pago");
            tableModel.addColumn("Detalles de Tarjeta");
            tableModel.addColumn("Otros Detalles");

            pagosTable = new JTable(tableModel);
            pagosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            pagosTable.setFont(new Font("Arial", Font.PLAIN, 12));
            pagosTable.setRowHeight(25);
            pagosTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            pagosTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    return c;
                }
            });

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            pagosTable.setRowSorter(sorter);

            JScrollPane scrollPane = new JScrollPane(pagosTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            // Panel Agregar/Modificar
            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Método de Pago", TitledBorder.LEFT, TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14), Color.BLACK));
            panelAgregar.setBackground(new Color(240, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField renterIdField = new JTextField(15);
            JTextField metodoPagoField = new JTextField(15);
            JTextField tarjetaField = new JTextField(15);
            JTextField otrosField = new JTextField(15);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panelAgregar.add(new JLabel("Renter ID*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(renterIdField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            panelAgregar.add(new JLabel("Método de Pago*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(metodoPagoField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            panelAgregar.add(new JLabel("Detalles de Tarjeta:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(tarjetaField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            panelAgregar.add(new JLabel("Otros Detalles:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(otrosField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            panelAgregar.add(new JLabel("* Campo obligatorio"), gbc);

            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton agregarButton = new JButton("Agregar");
            JButton modificarButton = new JButton("Modificar");
            JButton eliminarButton = new JButton("Eliminar");

            agregarButton.setBackground(new Color(70, 130, 180));
            modificarButton.setBackground(new Color(34, 139, 34));
            eliminarButton.setBackground(new Color(255, 69, 0));
            agregarButton.setForeground(Color.WHITE);
            modificarButton.setForeground(Color.WHITE);
            eliminarButton.setForeground(Color.WHITE);

            modificarButton.setEnabled(false);
            eliminarButton.setEnabled(false);

            pagosTable.getSelectionModel().addListSelectionListener(e -> {
                boolean seleccion = pagosTable.getSelectedRow() != -1;
                modificarButton.setEnabled(seleccion);
                eliminarButton.setEnabled(seleccion);
            });

            agregarButton.addActionListener(e -> {
                if (renterIdField.getText().isEmpty() || metodoPagoField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Los campos marcados con * son obligatorios.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                agregarPago(
                        Integer.parseInt(renterIdField.getText()), metodoPagoField.getText(),
                        tarjetaField.getText(), otrosField.getText()
                );
            });

            modificarButton.addActionListener(e -> {
                int selectedRow = pagosTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    modificarPago(id,
                            Integer.parseInt(renterIdField.getText()), metodoPagoField.getText(),
                            tarjetaField.getText(), otrosField.getText()
                    );
                }
            });

            eliminarButton.addActionListener(e -> {
                int selectedRow = pagosTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarPago(id);
                }
            });

            panelBotones.add(agregarButton);
            panelBotones.add(modificarButton);
            panelBotones.add(eliminarButton);
            gbc.gridy = 5;
            panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarPagos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarPagos() {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Renters_Payment_Methods");
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("Renter_Payment_Method_ID"),
                            rs.getInt("Renter_ID"),
                            rs.getString("Payment_Method_Code"),
                            rs.getString("Card_Details"),
                            rs.getString("Other_Details")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar métodos de pago.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void agregarPago(int renterId, String metodo, String tarjeta, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Renters_Payment_Methods (Renter_ID, Payment_Method_Code, Card_Details, Other_Details) VALUES (?, ?, ?, ?)");
                ps.setInt(1, renterId);
                ps.setString(2, metodo);
                ps.setString(3, tarjeta);
                ps.setString(4, otros);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Método de pago agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarPagos();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al agregar el método de pago.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void modificarPago(int id, int renterId, String metodo, String tarjeta, String otros) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE Renters_Payment_Methods SET Renter_ID=?, Payment_Method_Code=?, Card_Details=?, Other_Details=? WHERE Renter_Payment_Method_ID=?");
                ps.setInt(1, renterId);
                ps.setString(2, metodo);
                ps.setString(3, tarjeta);
                ps.setString(4, otros);
                ps.setInt(5, id);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Método de pago modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarPagos();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al modificar el método de pago.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void eliminarPago(int id) {
            try {
                PreparedStatement ps = connection.prepareStatement("DELETE FROM Renters_Payment_Methods WHERE Renter_Payment_Method_ID = ?");
                ps.setInt(1, id);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Método de pago eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0);
                    cargarPagos();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar el método de pago.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class PanelProcesos extends JPanel {
        private CardLayout cardLayout;
        private JPanel contentPanel;

        // Componentes compartidos
        private JComboBox<String> clientesComboBox;
        private JComboBox<String> bicicletasComboBox;
        private DefaultTableModel modeloCalculoPagos;
        private JTable tablaCalculoPagos;

        public PanelProcesos() {
            cardLayout = new CardLayout();
            contentPanel = new JPanel(cardLayout);
            setLayout(new BorderLayout());

            // Menú superior
            JMenuBar menuBar = new JMenuBar();
            JMenu menuProcesos = new JMenu("Procesos");

            JMenuItem registrarAlquiler = new JMenuItem("Registrar nuevo alquiler");
            registrarAlquiler.addActionListener(e -> mostrarPanel("registrarAlquiler"));
            menuProcesos.add(registrarAlquiler);

            JMenuItem calcularPagos = new JMenuItem("Calcular pagos");
            calcularPagos.addActionListener(e -> {
                cargarAlquileresPendientes();
                mostrarPanel("calcularPagos");
            });
            menuProcesos.add(calcularPagos);

            JMenuItem gestionBicicletas = new JMenuItem("Asignación/Devolución");
            gestionBicicletas.addActionListener(e -> {
                cargarBicicletasAsignacion();
                mostrarPanel("gestionBicicletas");
            });
            menuProcesos.add(gestionBicicletas);

            menuBar.add(menuProcesos);
            add(menuBar, BorderLayout.NORTH);

            // Panel de Registro de Alquileres (Proceso #1)
            JPanel registrarAlquilerPanel = crearPanelRegistroAlquiler();

            // Panel de Cálculo de Pagos (Proceso #2)
            JPanel calcularPagosPanel = crearPanelCalcularPagos();

            // Panel de Gestión de Bicicletas (Proceso #3)
            JPanel gestionBicicletasPanel = crearPanelGestionBicicletas();

            contentPanel.add(registrarAlquilerPanel, "registrarAlquiler");
            contentPanel.add(calcularPagosPanel, "calcularPagos");
            contentPanel.add(gestionBicicletasPanel, "gestionBicicletas");

            add(contentPanel, BorderLayout.CENTER);
            cardLayout.show(contentPanel, "registrarAlquiler");
        }

        private JPanel crearPanelRegistroAlquiler() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Alquiler"));
            panel.setBackground(new Color(230, 230, 250));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Componentes
            clientesComboBox = new JComboBox<>();
            JComboBox<String> bicicletasComboBox = new JComboBox<>();
            JComboBox<String> metodosPagoComboBox = new JComboBox<>();
            JComboBox<String> tarifasComboBox = new JComboBox<>(new String[]{"Por hora", "Por día"});
            JTextField fechaInicioField = new JTextField(20);
            fechaInicioField.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date()));
            JTextField fechaFinField = new JTextField(20);

            // Layout
            agregarComponente(panel, gbc, new JLabel("Cliente:"), 0, 0);
            agregarComponente(panel, gbc, clientesComboBox, 1, 0);
            agregarComponente(panel, gbc, new JLabel("Bicicleta:"), 0, 1);
            agregarComponente(panel, gbc, bicicletasComboBox, 1, 1);
            agregarComponente(panel, gbc, new JLabel("Método de Pago:"), 0, 2);
            agregarComponente(panel, gbc, metodosPagoComboBox, 1, 2);
            agregarComponente(panel, gbc, new JLabel("Tipo de Tarifa:"), 0, 3);
            agregarComponente(panel, gbc, tarifasComboBox, 1, 3);
            agregarComponente(panel, gbc, new JLabel("Fecha/Hora Inicio:"), 0, 4);
            agregarComponente(panel, gbc, fechaInicioField, 1, 4);
            agregarComponente(panel, gbc, new JLabel("Fecha/Hora Fin:"), 0, 5);
            agregarComponente(panel, gbc, fechaFinField, 1, 5);

            JButton registrarButton = new JButton("Registrar Alquiler");
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            panel.add(registrarButton, gbc);

            // Cargar datos
            cargarClientesEnComboBox(clientesComboBox);
            cargarBicicletasDisponiblesEnComboBox(bicicletasComboBox);
            cargarMetodosPagoEnComboBox(metodosPagoComboBox);


            // Eventos
            clientesComboBox.addActionListener(e -> {
                String cliente = (String) clientesComboBox.getSelectedItem();
                if (cliente != null && cliente.startsWith("Cliente #")) {
                    int renterId = Integer.parseInt(cliente.split("#")[1].split(" ")[0]);
                    cargarMetodosPagoEnComboBox(metodosPagoComboBox);
                }
            });

            registrarButton.addActionListener(e -> {
                try {
                    String cliente = (String) clientesComboBox.getSelectedItem();
                    String bicicleta = (String) bicicletasComboBox.getSelectedItem();
                    String metodoPago = (String) metodosPagoComboBox.getSelectedItem();
                    String tipoTarifa = (String) tarifasComboBox.getSelectedItem();
                    String fechaInicio = fechaInicioField.getText();
                    String fechaFin = fechaFinField.getText();

                    if (metodoPago == null || metodoPago.contains("--")) {
                        JOptionPane.showMessageDialog(null, "Seleccione un método de pago válido");
                        return;
                    }

                    int renterId = Integer.parseInt(cliente.split("#")[1].split(" ")[0]);
                    int bicycleId = Integer.parseInt(bicicleta.split("#")[1]);
                    int paymentMethodId = obtenerIdMetodoPago(metodoPago);

                    registrarNuevoAlquiler(renterId, bicycleId, paymentMethodId,
                            tipoTarifa.equals("Por día"), fechaInicio, fechaFin);

                    // Actualizar lista de bicicletas
                    cargarBicicletasDisponiblesEnComboBox(bicicletasComboBox);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            return panel;
        }

        private JPanel crearPanelCalcularPagos() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Cálculo de Pagos"));
            panel.setBackground(new Color(230, 230, 250));

            // Crear modelo y tabla
            modeloCalculoPagos = new DefaultTableModel(
                    new Object[]{"ID Alquiler", "Cliente", "Bicicleta", "Monto Due", "Estado"}, 0);
            tablaCalculoPagos = new JTable(modeloCalculoPagos);

            // Configurar la tabla para que sea visible y tenga buen tamaño
            tablaCalculoPagos.setPreferredScrollableViewportSize(new Dimension(500, 300));
            tablaCalculoPagos.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(tablaCalculoPagos);

            // Panel de controles
            JPanel controlesPanel = new JPanel(new FlowLayout());
            JButton calcularButton = new JButton("Calcular Pago");
            JButton marcarPagadoButton = new JButton("Marcar como Pagado");

            controlesPanel.add(calcularButton);
            controlesPanel.add(marcarPagadoButton);

            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(controlesPanel, BorderLayout.SOUTH);

            // Eventos
            calcularButton.addActionListener(e -> {
                int fila = tablaCalculoPagos.getSelectedRow();
                if (fila >= 0) {
                    int rentalId = (int) modeloCalculoPagos.getValueAt(fila, 0);
                    calcularPagoAlquiler(rentalId);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un alquiler");
                }
            });

            marcarPagadoButton.addActionListener(e -> {
                int fila = tablaCalculoPagos.getSelectedRow();
                if (fila >= 0) {
                    int rentalId = (int) modeloCalculoPagos.getValueAt(fila, 0);
                    marcarAlquilerPagado(rentalId);
                    cargarAlquileresPendientes(); // Recargar los datos después de marcar como pagado
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un alquiler");
                }
            });

            // Cargar datos iniciales
            cargarAlquileresPendientes();

            return panel;
        }


        private JPanel crearPanelGestionBicicletas() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Gestión de Bicicletas"));
            panel.setBackground(new Color(230, 230, 250));

            // Tabla de bicicletas
            DefaultTableModel model = new DefaultTableModel(
                    new Object[]{"ID Bicicleta", "Estado", "Tienda", "Fecha Asignación"}, 0);
            JTable tablaBicicletas = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(tablaBicicletas);

            // Panel de controles
            JPanel controlesPanel = new JPanel(new FlowLayout());
            JComboBox<String> tiendasComboBox = new JComboBox<>();
            JButton asignarButton = new JButton("Asignar a Tienda");
            JButton devolverButton = new JButton("Devolver a Inventario");

            controlesPanel.add(new JLabel("Tienda:"));
            controlesPanel.add(tiendasComboBox);
            controlesPanel.add(asignarButton);
            controlesPanel.add(devolverButton);

            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(controlesPanel, BorderLayout.SOUTH);

            // Cargar tiendas
            cargarTiendasEnComboBox(tiendasComboBox);

            // Eventos
            asignarButton.addActionListener(e -> {
                int fila = tablaBicicletas.getSelectedRow();
                if (fila >= 0 && tiendasComboBox.getSelectedIndex() >= 0) {
                    int bicycleId = (int) model.getValueAt(fila, 0);
                    int shopId = obtenerIdTienda((String) tiendasComboBox.getSelectedItem());
                    asignarBicicletaATienda(bicycleId, shopId);
                    cargarBicicletasAsignacion();
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una bicicleta y una tienda");
                }
            });

            devolverButton.addActionListener(e -> {
                int fila = tablaBicicletas.getSelectedRow();
                if (fila >= 0) {
                    int bicycleId = (int) model.getValueAt(fila, 0);
                    devolverBicicleta(bicycleId);
                    cargarBicicletasAsignacion();
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una bicicleta");
                }
            });

            return panel;
        }

        // Métodos de carga de datos
        private void cargarClientesEnComboBox(JComboBox<String> comboBox) {
            try (Connection conn = ConexionBD.getConexion();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT Renter_ID, Registration_Date_Time FROM Renters")) {

                comboBox.removeAllItems();
                while (rs.next()) {
                    comboBox.addItem("Cliente #" + rs.getInt("Renter_ID") + " - " +
                            rs.getTimestamp("Registration_Date_Time"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar clientes");
            }
        }

        private void cargarBicicletasDisponiblesEnComboBox(JComboBox<String> comboBox) {
            String sql = "SELECT Bicycle_ID FROM Bicycles WHERE Bicycle_ID NOT IN " +
                    "(SELECT Bicycle_ID FROM Rentals WHERE Actual_End_Date_Time IS NULL)";

            try (Connection conn = ConexionBD.getConexion();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                comboBox.removeAllItems();
                while (rs.next()) {
                    comboBox.addItem("Bicicleta #" + rs.getInt("Bicycle_ID"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar bicicletas disponibles");
            }
        }

        private void cargarMetodosPagoEnComboBox(JComboBox<String> comboBox) {
            // Obtener el cliente seleccionado del ComboBox
            Object selectedItem = clientesComboBox.getSelectedItem(); // clientesComboBox debe ser accesible aquí

            if (selectedItem == null) {
                comboBox.removeAllItems();
                comboBox.addItem("-- Selecciona un cliente primero --");
                return;
            }

            // Extraer el Renter_ID del string, por ejemplo: "Cliente #123"
            String clienteStr = selectedItem.toString();
            int renterId;
            try {
                if (clienteStr.contains("#")) {
                    String[] parts = clienteStr.split("#");
                    renterId = Integer.parseInt(parts[1].trim());
                } else {
                    throw new NumberFormatException("Formato inesperado: " + clienteStr);
                }
            } catch (NumberFormatException e) {
                comboBox.removeAllItems();
                comboBox.addItem("-- Formato inválido de cliente --");
                return;
            }

            // Consulta a la base de datos usando el renterId obtenido
            String sql = "SELECT rpm.Renter_Payment_Method_ID, rpm.Payment_Method_Code, rpm.Card_Details " +
                    "FROM Renters_Payment_Methods rpm " +
                    "WHERE rpm.Renter_ID = ?";

            try (Connection conn = ConexionBD.getConexion();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, renterId);
                ResultSet rs = pstmt.executeQuery();

                comboBox.removeAllItems();
                while (rs.next()) {
                    String texto = String.format("%s - %s (ID: %d)",
                            rs.getString("Payment_Method_Code"),
                            rs.getString("Card_Details") != null ? rs.getString("Card_Details") : "Sin detalles",
                            rs.getInt("Renter_Payment_Method_ID"));
                    comboBox.addItem(texto);
                }

                if (comboBox.getItemCount() == 0) {
                    comboBox.addItem("-- No hay métodos de pago --");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                comboBox.removeAllItems();
                comboBox.addItem("-- Error al cargar --");
            }
        }


        private void cargarAlquileresPendientes() {
            if (modeloCalculoPagos == null) {
                System.out.println("El modelo de tabla es null");
                return;
            }

            modeloCalculoPagos.setRowCount(0); // Limpiar la tabla

            String sql = "SELECT r.Rental_ID, re.Renter_ID, b.Bicycle_ID, r.Rental_Payment_Due, r.Payment_status_Code " +
                    "FROM Rentals r " +
                    "JOIN Renters re ON r.Renter_ID = re.Renter_ID " +
                    "JOIN Bicycles b ON r.Bicycle_ID = b.Bicycle_ID " +
                    "WHERE r.Payment_status_Code = 'PEND'";  // ✅ Valor corregido

            try (Connection conn = ConexionBD.getConexion();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                int count = 0;
                while (rs.next()) {
                    count++;
                    modeloCalculoPagos.addRow(new Object[] {
                            rs.getInt("Rental_ID"),
                            "Cliente #" + rs.getInt("Renter_ID"),
                            "Bicicleta #" + rs.getInt("Bicycle_ID"),
                            rs.getDouble("Rental_Payment_Due"),
                            rs.getString("Payment_status_Code")
                    });
                }

                System.out.println("Se cargaron " + count + " alquileres pendientes.");

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar los alquileres pendientes");
            }
        }


        private void cargarBicicletasAsignacion() {
            DefaultTableModel model = (DefaultTableModel) ((JTable)((JScrollPane)((JPanel)contentPanel.getComponent(2))
                    .getComponent(0)).getViewport().getView()).getModel();
            model.setRowCount(0);

            String sql = "SELECT b.Bicycle_ID, " +
                    "CASE WHEN bis.DateTime_Out IS NULL THEN 'Asignada' ELSE 'Disponible' END as Estado, " +
                    "COALESCE(ms.Location_Name, 'No asignada') as Tienda, " +
                    "MAX(bis.DateTime_In) as Fecha_Asignacion " +
                    "FROM Bicycles b " +
                    "LEFT JOIN Bicycles_in_Shops bis ON b.Bicycle_ID = bis.Bicycle_ID " +
                    "LEFT JOIN Multi_Shop ms ON bis.Multi_Shop_ID = ms.Multi_Shop_ID " +
                    "GROUP BY b.Bicycle_ID, Estado, Tienda";

            try (Connection conn = ConexionBD.getConexion();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("Bicycle_ID"),
                            rs.getString("Estado"),
                            rs.getString("Tienda"),
                            rs.getTimestamp("Fecha_Asignacion")
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar bicicletas");
            }
        }

        private void cargarTiendasEnComboBox(JComboBox<String> comboBox) {
            try (Connection conn = ConexionBD.getConexion();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT Multi_Shop_ID, Location_Name FROM Multi_Shop")) {

                comboBox.removeAllItems();
                while (rs.next()) {
                    comboBox.addItem("Tienda #" + rs.getInt("Multi_Shop_ID") + " - " + rs.getString("Location_Name"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar tiendas");
            }
        }

        // Métodos de operaciones
        private void registrarNuevoAlquiler(int renterId, int bicycleId, int paymentMethodId,
                                            boolean esDiaria, String fechaInicio, String fechaFin) {
            try (Connection conn = ConexionBD.getConexion()) {
                // Obtener tarifa
                double tarifa = esDiaria ? obtenerTarifaDiaria(conn) : obtenerTarifaHora(conn);
                double monto = calcularMontoAlquiler(esDiaria, fechaInicio, fechaFin, tarifa);

                // Insertar alquiler
                String sql = "INSERT INTO Rentals (Bicycle_ID, Payment_status_Code, Rental_Rates_ID, " +
                        "Renter_ID, Renter_Payment_Method_ID, All_Day_Rental_YN, " +
                        "Booked_Start_Date_Time, Booked_End_Date_Time, Rental_Payment_Due) " +
                        "VALUES (?, 'PENDIENTE', ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, bicycleId);
                    pstmt.setInt(2, esDiaria ? 1 : 2); // IDs de tarifas asumidos
                    pstmt.setInt(3, renterId);
                    pstmt.setInt(4, paymentMethodId);
                    pstmt.setString(5, esDiaria ? "Y" : "N");
                    pstmt.setTimestamp(6, Timestamp.valueOf(fechaInicio));
                    pstmt.setTimestamp(7, Timestamp.valueOf(fechaFin));
                    pstmt.setDouble(8, monto);

                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Alquiler registrado. Monto a pagar: $" + monto);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al registrar alquiler: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }

        private void calcularPagoAlquiler(int rentalId) {
            try (Connection conn = ConexionBD.getConexion()) {
                String sql = "SELECT Rental_Payment_Due, Rental_Payment_Made, " +
                        "(Rental_Payment_Due - Rental_Payment_Made) AS Saldo " +
                        "FROM Rentals WHERE Rental_ID = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, rentalId);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                String.format("Detalles de pago:\nMonto Total: $%.2f\nPagado: $%.2f\nSaldo: $%.2f",
                                        rs.getDouble("Rental_Payment_Due"),
                                        rs.getDouble("Rental_Payment_Made"),
                                        rs.getDouble("Saldo")));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al calcular pago");
            }
        }


        private void marcarAlquilerPagado(int rentalId) {
            try (Connection conn = ConexionBD.getConexion()) {
                String sql = "UPDATE Rentals SET Payment_status_Code = 'PAID', " +
                        "Rental_Payment_Made = Rental_Payment_Due " +
                        "WHERE Rental_ID = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, rentalId);
                    int updated = pstmt.executeUpdate();

                    if (updated > 0) {
                        JOptionPane.showMessageDialog(null, "Alquiler marcado como pagado");
                        cargarAlquileresPendientes(); // Recargar la tabla
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al marcar como pagado");
            }
        }


        private void asignarBicicletaATienda(int bicycleId, int shopId) {
            try (Connection conn = ConexionBD.getConexion()) {
                // Primero marcar cualquier asignación previa como devuelta
                String updateSql = "UPDATE Bicycles_in_Shops SET DateTime_Out = CURRENT_TIMESTAMP " +
                        "WHERE Bicycle_ID = ? AND DateTime_Out IS NULL";

                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, bicycleId);
                    updateStmt.executeUpdate();
                }

                // Insertar nueva asignación
                String insertSql = "INSERT INTO Bicycles_in_Shops (Multi_Shop_ID, Bicycle_ID) VALUES (?, ?)";

                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, shopId);
                    insertStmt.setInt(2, bicycleId);
                    insertStmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Bicicleta asignada a tienda");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al asignar bicicleta");
            }
        }

        private void devolverBicicleta(int bicycleId) {
            try (Connection conn = ConexionBD.getConexion()) {
                String sql = "UPDATE Bicycles_in_Shops SET DateTime_Out = CURRENT_TIMESTAMP " +
                        "WHERE Bicycle_ID = ? AND DateTime_Out IS NULL";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, bicycleId);
                    int updated = pstmt.executeUpdate();

                    if (updated > 0) {
                        JOptionPane.showMessageDialog(null, "Bicicleta devuelta al inventario");
                    } else {
                        JOptionPane.showMessageDialog(null, "La bicicleta no estaba asignada");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al devolver bicicleta");
            }
        }

        // Métodos auxiliares
        private int obtenerIdMetodoPago(String metodoPago) {
            try {
                String idStr = metodoPago.substring(metodoPago.lastIndexOf("ID: ") + 4);
                idStr = idStr.replace(")", "").trim();
                return Integer.parseInt(idStr);
            } catch (Exception ex) {
                throw new RuntimeException("Formato de método de pago inválido");
            }
        }

        private int obtenerIdTienda(String tienda) {
            try {
                String idStr = tienda.split("#")[1].split(" ")[0];
                return Integer.parseInt(idStr);
            } catch (Exception ex) {
                throw new RuntimeException("Formato de tienda inválido");
            }
        }

        private double obtenerTarifaDiaria(Connection conn) throws SQLException {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT Daily_Rate FROM Rental_Rates LIMIT 1")) {
                if (rs.next()) return rs.getDouble(1);
                throw new SQLException("No se encontró tarifa diaria");
            }
        }

        private double obtenerTarifaHora(Connection conn) throws SQLException {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT Hourly_Rate FROM Rental_Rates LIMIT 1")) {
                if (rs.next()) return rs.getDouble(1);
                throw new SQLException("No se encontró tarifa por hora");
            }
        }

        private double calcularMontoAlquiler(boolean esDiaria, String fechaInicioStr, String fechaFinStr, double tarifa) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date fechaInicio = (Date) format.parse(fechaInicioStr);
                Date fechaFin = (Date) format.parse(fechaFinStr);

                long diferencia = fechaFin.getTime() - fechaInicio.getTime();
                double horas = diferencia / (1000.0 * 60 * 60);

                return esDiaria ? Math.ceil(horas / 24) * tarifa : horas * tarifa;
            } catch (Exception ex) {
                throw new RuntimeException("Error en formato de fecha: yyyy-MM-dd HH:mm");
            }
        }

        private void agregarComponente(JPanel panel, GridBagConstraints gbc, Component comp, int x, int y) {
            gbc.gridx = x;
            gbc.gridy = y;
            panel.add(comp, gbc);
        }

        private void mostrarPanel(String panelName) {
            cardLayout.show(contentPanel, panelName);
        }
    }

    static class PanelReportes extends JPanel {
        public PanelReportes() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("Gestión de Reportes");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            add(titleLabel, gbc);
            gbc.gridwidth = 1;

            int fila = 1;

            // Reportes básicos
            fila = agregarBotonReporte("Alquileres por Cliente", "Genera un reporte de todos los alquileres por cliente.",
                    this::generarReporteAlquileresPorCliente, gbc, fila);
            fila = agregarBotonReporte("Ingresos por Tienda", "Reporte de ingresos generados por tienda.",
                    this::generarReporteIngresosPorTienda, gbc, fila);
            fila = agregarBotonReporte("Bicicletas Más Rentadas", "Bicicletas con más alquileres registrados.",
                    this::generarReporteBicicletasMasRentadas, gbc, fila);

            // Reportes por tabla
            fila = agregarBotonReporte("Clientes (Renters)", "Listado completo de clientes registrados.",
                    this::generarReporteRenters, gbc, fila);
            fila = agregarBotonReporte("Métodos de Pago", "Listado de métodos de pago de clientes.",
                    this::generarReportePaymentMethods, gbc, fila);
            fila = agregarBotonReporte("Bicicletas", "Listado de bicicletas en el sistema.",
                    this::generarReporteBicycles, gbc, fila);
            fila = agregarBotonReporte("Bicicletas en Tiendas", "Ubicaciones de bicicletas por tienda.",
                    this::generarReporteBicyclesInShops, gbc, fila);
            fila = agregarBotonReporte("Tiendas (Multi_Shop)", "Tiendas registradas en el sistema.",
                    this::generarReporteMultiShop, gbc, fila);
        }

        private int agregarBotonReporte(String titulo, String descripcion, Runnable accion,
                                        GridBagConstraints gbc, int fila) {
            JButton boton = new JButton(titulo);
            gbc.gridx = 0;
            gbc.gridy = fila;
            add(boton, gbc);

            JLabel desc = new JLabel(descripcion);
            gbc.gridx = 1;
            add(desc, gbc);

            boton.addActionListener(e -> accion.run());
            return fila + 1;
        }

        private String obtenerNombreArchivo(String baseNombre) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return baseNombre + "_" + fecha + ".txt";
        }

        private Connection conectar() throws SQLException {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/bicycle_rental",
                    "developer", "23100132");
        }

        // Métodos de generación de reportes
        private void generarReporteAlquileresPorCliente() {
            String archivo = obtenerNombreArchivo("reporte_alquileres_por_cliente");
            try (BufferedWriter w = new BufferedWriter(new FileWriter(archivo));
                 Connection c = conectar();
                 Statement s = c.createStatement();
                 ResultSet r = s.executeQuery("""
                             SELECT Renter_ID, COUNT(*) AS total, SUM(Rental_Payment_Due) AS total_pago
                             FROM Rentals GROUP BY Renter_ID ORDER BY total DESC
                         """)) {
                while (r.next()) {
                    w.write("Cliente ID: " + r.getInt(1) + ", Total alquileres: " + r.getInt(2)
                            + ", Total pago: $" + r.getDouble(3));
                    w.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void generarReporteIngresosPorTienda() {
            String archivo = obtenerNombreArchivo("reporte_ingresos_por_tienda");
            try (BufferedWriter w = new BufferedWriter(new FileWriter(archivo));
                 Connection c = conectar();
                 Statement s = c.createStatement();
                 ResultSet r = s.executeQuery("""
                             SELECT ms.Multi_Shop_ID, ms.Location_Name, SUM(r.Rental_Payment_Made) AS ingresos
                             FROM Rentals r
                             JOIN Bicycles b ON b.Bicycle_ID = r.Bicycle_ID
                             JOIN Bicycles_in_Shops bis ON bis.Bicycle_ID = b.Bicycle_ID
                             JOIN Multi_Shop ms ON ms.Multi_Shop_ID = bis.Multi_Shop_ID
                             GROUP BY ms.Multi_Shop_ID, ms.Location_Name
                         """)) {
                while (r.next()) {
                    w.write("Tienda: " + r.getString(2) + " (ID: " + r.getInt(1) + "), Ingresos: $" + r.getDouble(3));
                    w.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void generarReporteBicicletasMasRentadas() {
            String archivo = obtenerNombreArchivo("reporte_bicicletas_mas_rentadas");
            try (BufferedWriter w = new BufferedWriter(new FileWriter(archivo));
                 Connection c = conectar();
                 Statement s = c.createStatement();
                 ResultSet r = s.executeQuery("""
                             SELECT b.Bicycle_ID, b.Bicycle_Details, COUNT(*) AS total
                             FROM Bicycles b
                             JOIN Rentals r ON b.Bicycle_ID = r.Bicycle_ID
                             GROUP BY b.Bicycle_ID, b.Bicycle_Details
                             ORDER BY total DESC
                         """)) {
                while (r.next()) {
                    w.write("Bicicleta ID: " + r.getInt(1) + ", Rentas: " + r.getInt(3) + ", Detalles: " + r.getString(2));
                    w.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void generarReporteRenters() {
            String archivo = obtenerNombreArchivo("reporte_renters");
            String sql = "SELECT Renter_ID, Registration_Date_Time, Last_Rental_Date_Time, Other_Details FROM Renters";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
                 Connection conn = conectar();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                writer.write("ID,Fecha Registro,Último Alquiler,Detalles\n");
                while (rs.next()) {
                    int id = rs.getInt("Renter_ID");
                    Timestamp registro = rs.getTimestamp("Registration_Date_Time");
                    Timestamp ultimo = rs.getTimestamp("Last_Rental_Date_Time");
                    String detalles = rs.getString("Other_Details");
                    writer.write(id + "," + registro + "," + (ultimo != null ? ultimo : "") + "," + detalles + "\n");
                }
                JOptionPane.showMessageDialog(this, "Reporte de renters generado.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar reporte de renters: " + e.getMessage());
            }
        }



        private void generarReportePaymentMethods() {
            String archivo = obtenerNombreArchivo("reporte_payment_methods");
            String sql = """
        SELECT rpm.Renter_Payment_Method_ID, rpm.Renter_ID, rpm.Payment_Method_Code, rpd.Payment_Method_Description, rpm.Card_Details
        FROM Renters_Payment_Methods rpm
        JOIN Ref_Payment_Methods rpd ON rpm.Payment_Method_Code = rpd.Payment_Method_Code
        """;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
                 Connection conn = conectar();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                writer.write("ID Método,ID Renter,Código,Descripción,Detalles Tarjeta\n");
                while (rs.next()) {
                    int id = rs.getInt("Renter_Payment_Method_ID");
                    int renterId = rs.getInt("Renter_ID");
                    String code = rs.getString("Payment_Method_Code");
                    String desc = rs.getString("Payment_Method_Description");
                    String card = rs.getString("Card_Details");
                    writer.write(id + "," + renterId + "," + code + "," + desc + "," + card + "\n");
                }
                JOptionPane.showMessageDialog(this, "Reporte de métodos de pago generado.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar reporte de métodos de pago: " + e.getMessage());
            }
        }



        private void generarReporteBicycles() {
            String archivo = obtenerNombreArchivo("reporte_bicycles");
            String sql = "SELECT Bicycle_ID, Bicycle_Details FROM Bicycles";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
                 Connection conn = conectar();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                writer.write("ID Bicicleta,Detalles\n");
                while (rs.next()) {
                    int id = rs.getInt("Bicycle_ID");
                    String detalles = rs.getString("Bicycle_Details");
                    writer.write(id + "," + detalles + "\n");
                }
                JOptionPane.showMessageDialog(this, "Reporte de bicicletas generado.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar reporte de bicicletas: " + e.getMessage());
            }
        }



        private void generarReporteBicyclesInShops() {
            String archivo = obtenerNombreArchivo("reporte_bicycles_tiendas");
            String sql = """
        SELECT ms.Multi_Shop_ID, ms.Contact_Name, COUNT(*) AS Bicycle_Count
        FROM Multi_Shop ms
        JOIN Bicycles_in_Shops bis ON ms.Multi_Shop_ID = bis.Multi_Shop_ID
        GROUP BY ms.Multi_Shop_ID, ms.Contact_Name
        """;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
                 Connection conn = conectar();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                writer.write("ID Tienda,Nombre Contacto,Cantidad Bicicletas\n");
                while (rs.next()) {
                    int id = rs.getInt("Multi_Shop_ID");
                    String contacto = rs.getString("Contact_Name");
                    int cantidad = rs.getInt("Bicycle_Count");
                    writer.write(id + "," + contacto + "," + cantidad + "\n");
                }
                JOptionPane.showMessageDialog(this, "Reporte de bicicletas por tienda generado.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar reporte: " + e.getMessage());
            }
        }



        private void generarReporteMultiShop() {
            String archivo = obtenerNombreArchivo("reporte_multishop");
            String sql = "SELECT Multi_Shop_ID, Contact_Name, Location_Name FROM Multi_Shop";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
                 Connection conn = conectar();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                writer.write("ID Tienda,Nombre Contacto,Ubicación\n");
                while (rs.next()) {
                    int id = rs.getInt("Multi_Shop_ID");
                    String contacto = rs.getString("Contact_Name");
                    String ubicacion = rs.getString("Location_Name");
                    writer.write(id + "," + contacto + "," + ubicacion + "\n");
                }
                JOptionPane.showMessageDialog(this, "Reporte de tiendas generado.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar reporte de tiendas: " + e.getMessage());
            }
        }

    }
}

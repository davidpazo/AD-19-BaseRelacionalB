package BaseRelacional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author oracle
 */
public class BaseRelacionalB {

    private Connection conexion;

    String tabla = "productos";
    Statement st;
    ResultSet rs;

    public static void main(String[] args) {
        BaseRelacionalB brb = new BaseRelacionalB().conectar();
        int opc;
        do {
            opc = JOptionPane.showOptionDialog(
                    null, "MENU", "que desea hacer?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                        "Ejecutar",
                        "Consultar",
                        "Actualizar Precio",
                        "Borrar",
                        "Insertar nuevo",
                        "Salir"},
                    "Exit") + 1;

            switch (opc) {
                case 1:
                    brb.insertar("p1", "parafusos", 3);
                    brb.insertar("p2", "cravos", 4);
                    brb.insertar("p3", "tachas", 6);
                    break;
                case 2:
                    brb.consultar();
                    break;
                case 3:
                    brb.modificar("p1", 9);
                    break;
                case 4:
                    brb.Borrar("p3");
                    break;
                case 5:
                    brb.insertarRS("p4", "tetris", 20);
                    break;
                case 6:
                    System.exit(0);
            }
        } while (opc != 0 && opc != 6);
    }

    public BaseRelacionalB conectar() {
        try {
            String BaseDeDatos = "jdbc:oracle:thin:@localhost:1521:orcl";
            conexion = DriverManager.getConnection(BaseDeDatos, "hr", "hr");
            if (conexion != null) {
                System.out.println("Conexion exitosa!");
            } else {
                System.out.println("Conexion fallida!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public void insertar(String codigo, String descricion, int prezo) {
        try {
            System.out.println(">>Metiendo nueva fila...");
            String consulta = "insert into productos values ('" + codigo + "','" + descricion + "'," + prezo + ")";
            st = conexion.createStatement();
            st.executeUpdate(consulta);
        } catch (SQLException ex) {
            System.out.println("SQLException " + ex);
        }

    }

    private void modifiRS() {
        try {
            String consulta = "Select codigo, descricion, prezo from productos";
            st = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(consulta);
        } catch (SQLException ex) {
            System.out.println("SQLException" + ex);
        }
    }

    public void consultar() {
        try {
            System.out.println(">>Se procede a consultar la tabla productos");
            String consulta = "Select * from productos";
            st = conexion.createStatement();
            rs = st.executeQuery(consulta);
            System.out.println("Listado de elementos en " + tabla + ": ");
            Recorrer();

        } catch (SQLException ex) {
            Logger.getLogger(BaseRelacionalB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificar(String cod, int valor) {
        try {
            modifiRS();
            while (rs.next()) {
                String aux = rs.getString("codigo");
                if (aux.equalsIgnoreCase(cod)) {
                    rs.updateInt("prezo", valor);
                    rs.updateRow();
                    System.out.println("Tabla con los nuevos valores modificados: ");
                    rs.first();
                    rs.previous();
                    break;
                }
            }
            Recorrer();//imprimimos los valores del result set

        } catch (SQLException ex) {
            System.out.println("SQLException " + ex);
        }
    }

    public void insertarRS(String cod, String des, int prezo) {
        try {
            modifiRS();
            //Nueva insercion
            rs.moveToInsertRow();
            rs.updateString("codigo", cod);
            rs.updateString("descricion", des);
            rs.updateInt("prezo", prezo);
            rs.insertRow();

            System.err.println("Nuevo productos insertado: ");
            //Vemos todo el contenido
            consultar();

        } catch (SQLException ex) {
            System.out.println("SQLException " + ex);
        }
    }

    public void Borrar(String cod) {
        try {
            String consulta = "delete from productos";
            st = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(consulta);
            System.out.println("Productos borrados");

        } catch (SQLException ex) {
            System.out.println("SQLException" + ex);
        }
    }

    //Método que recorre el ResultSet. 
    private void Recorrer() {
        try {
            while (rs.next()) {
                String cod = rs.getString("codigo");
                String des = rs.getString("descricion");
                int prezo = rs.getInt("prezo");
                System.out.println(cod + " " + des + " " + prezo);
            }

        } catch (SQLException ex) {
            System.out.println("SQLException " + ex);
        }
    }
}

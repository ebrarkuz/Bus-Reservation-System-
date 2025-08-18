import java.util.Arrays;

public class Standard extends Bus{
    private int refundCut;

    public int getRefundCut() {
        return refundCut;
    }

    public void setRefundCut(int refundCut) {
        this.refundCut = refundCut;
    }

    public Standard(String komut, String type, int id, String from, String to, int rows, float price, char[][] busPlan, int refundCut) {
        super(komut, type, id, from, to, rows, price, busPlan);
        this.refundCut = refundCut;
    }
    @Override
    public Bus[] createBuses(String[] bustype) {
        Bus[] buses = new Bus[bustype.length];
        for (int i = 0; i < bustype.length; i++) {
            String[] tokens = bustype[i].split("\t");


            if (tokens.length != 8) {
                System.out.println("Satırda geçersiz token sayısı: " + bustype[i]);
                continue;
            }

            String komut = tokens[0];
            String type = tokens[1];
            int id = Integer.parseInt(tokens[2]);
            String from = tokens[3];
            String to = tokens[4];
            int rows = Integer.parseInt(tokens[5]);
            float price = Float.parseFloat(tokens[6]);
            int refundCut = Integer.parseInt(tokens[7]);

            buses[i] = new Standard(komut, type, id, from, to, rows, price, busPlan, refundCut);
        }
        return buses;
    }
    public void createSeatLayout() {
        busPlan = new char[rows][4];
        for (int i = 0; i < rows; i++) {
            busPlan[i][0] = '*';
            busPlan[i][1] = '*';
            busPlan[i][2] = '*';
            busPlan[i][3] = '*';
        }
    }
    public static void printBusMatrix(Bus bus) {
        char[][] busPlan = bus.getBusPlan();
        boolean isEmpty = true;


        for (int i = 0; i < busPlan.length; i++) {
            for (int j = 0; j < busPlan[i].length; j++) {
                if (busPlan[i][j] != ' ') {
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }


        if (isEmpty) {
            System.out.println("----------------");
            System.out.println("No Voyages Available!");
            System.out.println("----------------");
            return;
        }

        System.out.println("Voyage " + bus.getId());
        System.out.println(bus.getFrom() + "-" + bus.getTo());

        for (char[] row : bus.getBusPlan()) {
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if (i == 1) {
                    System.out.print(" |");
                }

                if (i != row.length - 1 && row[i] != ' ') {
                    System.out.print(" ");
                }
            }

            System.out.println();
        }
    }




}

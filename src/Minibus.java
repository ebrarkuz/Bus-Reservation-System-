import java.util.Arrays;

public class Minibus extends Bus {
    public Minibus(String komut, String type, int id, String from, String to, int rows, float price, char[][] busPlan) {
        super(komut, type, id, from, to, rows, price, busPlan);
    }

    @Override
    public Bus[] createBuses(String[] bustype) {
        return super.createBuses(bustype);
    }

    public void createSeatLayout() {
        busPlan = new char[rows][2];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(busPlan[i], '*');
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


        if (busPlan==null) {
            System.out.println("----------------");
            System.out.println("No Voyages Available!");
            System.out.println("----------------");
        }


        System.out.println("Voyage " + bus.getId());
        System.out.println(bus.getFrom() + "-" + bus.getTo());

        for (char[] row : bus.getBusPlan()) {
            boolean isFirstSeat = true;
            for (int i = 0; i < row.length; i++) {
                char seat = row[i];
                System.out.print(seat);
                if (i != row.length - 1 && seat != ' ') {
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
    }
}

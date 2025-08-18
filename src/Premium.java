import java.util.Arrays;

public class Premium extends Bus{
    private float refundCut;
    private float premiumFee;
    private float premiumPrice ;

    public float getPremiumPrice() {
        return premiumPrice;
    }

    public void setPremiumPrice(float premiumPrice) {
        this.premiumPrice = premiumPrice;
    }

    public float getRefundCut() {
        return refundCut;
    }

    public void setRefundCut(float refundCut) {
        this.refundCut = refundCut;
    }

    public float getPremiumFee() {
        return premiumFee;
    }

    public void setPremiumFee(float premiumFee) {
        this.premiumFee = premiumFee;
    }

    public Premium(String komut, String type, int id, String from, String to, int rows, float price, char[][] busPlan, float refundCut, float premiumFee, float premiumPrice) {
        super(komut, type, id, from, to, rows, price, busPlan);
        this.refundCut = refundCut;
        this.premiumFee = premiumFee;
        this.premiumPrice=premiumPrice;
    }

    @Override
    public Bus[] createBuses(String[] bustype) {
        Bus[] buses = super.createBuses(bustype);


        for (int i = 0; i < bustype.length; i++) {
            String[] tokens = bustype[i].split("\t");


            if (tokens.length != 9) {
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
            float refundCut = Float.parseFloat(tokens[7]);
            float premiumFee = Float.parseFloat(tokens[8]);

            buses[i] = new Premium(komut, type, id, from, to, rows, price, busPlan, refundCut, premiumFee,premiumPrice);
        }

        return buses;
    }
    public void createSeatLayout() {
        busPlan = new char[rows][3];
        for (int i = 0; i < rows; i++) {
            busPlan[i][0] = '*';
            busPlan[i][1] = '*';
            busPlan[i][2] = '*';
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

        if (busPlan == null) {
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
                if (isFirstSeat && seat != ' ') {
                    System.out.print(" |");
                    isFirstSeat = false;
                }

                if (i != row.length - 1 && seat != ' ') {
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
    }}




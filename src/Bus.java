public class Bus {
    private String komut;
    private String type;
    private int id;
    private String from;
    private String to;
    protected int rows;
    protected float price;
    protected char[][] busPlan;
    private boolean cancelled;
    private double revenue;

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


    public String getKomut() {
        return komut;
    }

    public void setKomut(String komut) {
        this.komut = komut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public char[][] getBusPlan() {
        return busPlan;
    }

    public void setBusPlan(char[][] busPlan) {
        this.busPlan = busPlan;
    }

    public Bus(String komut, String type, int id, String from, String to, int rows, float price, char[][] busPlan) {
        this.komut = komut;
        this.type = type;
        this.id = id;
        this.from = from;
        this.to = to;
        this.rows = rows;
        this.price = price;
        this.busPlan = busPlan;
    }

    /**
     *
     * @param bustype creating bus lists according to type it is overriding  in the subclasses
     * @return the buss list
     */
    public  Bus[] createBuses(String[] bustype) {
        Bus[] buses = new Bus[bustype.length];
        for (int i = 0; i < bustype.length; i++) {
            String[] tokens = bustype[i].split("\t");

            if (tokens.length != 7) {
                System.out.println("ERROR: invalid input line format " + bustype[i]);
                continue;
            }

            String komut = tokens[0];
            String type = tokens[1];
            int id = Integer.parseInt(tokens[2]);
            String from = tokens[3];
            String to = tokens[4];
            int rows = Integer.parseInt(tokens[5]);
            float price = Float.parseFloat(tokens[6]);
            buses[i] = new Bus(komut, type, id, from, to, rows, price,busPlan);
        }
        return buses;
    }

    /**
     *
     * @param seatNumber look at the seat is it sold already or not
     * @return true or false
     */
    public boolean sellTicket(int seatNumber) {
        int row = (seatNumber - 1) / busPlan[0].length;
        int column = (seatNumber - 1) % busPlan[0].length;

        if (row < 0 || row >= rows || column < 0 || column >= busPlan[0].length) {
            return false;
        }
        if(busPlan[row][column]=='|'){
            return false;

        }

        if (busPlan[row][column] == 'X') {
            return false;
        }

        busPlan[row][column] = 'X';
        return true;
    }
    public boolean refundTicket(int seatNumber) {
        int row = (seatNumber - 1) / busPlan[0].length;
        int column = (seatNumber - 1) % busPlan[0].length;

        if (row < 0 || row >= rows || column < 0 || column >= busPlan[0].length) {
            return false;
        }

        if (busPlan[row][column] == 'X') {
            busPlan[row][column] = '*';
            return true;
        } else {
            return false;
        }
    }
    public boolean isSeatSold(int seatNumber) {
        int row = (seatNumber - 1) / busPlan[0].length;
        int column = (seatNumber - 1) % busPlan[0].length;

        if (row < 0 || row >= rows || column < 0 || column >= busPlan[0].length) {
            return false;
        }

        return busPlan[row][column] == 'X';
    }
    public boolean isSeatEmpty(int seatNumber) {
        int row = (seatNumber - 1) / busPlan[0].length;
        int column = (seatNumber - 1) % busPlan[0].length;

        if (row < 0 || row >= rows || column < 0 || column >= busPlan[0].length) {
            return false;
        }

        return busPlan[row][column] == '*';
    }




}




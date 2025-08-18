import java.util.*;
import java.util.ArrayList;

public class MakeBus {
    private static List<Bus> busList;

    /**
     *
     * @param inputLines read command from input line and call the true metod
     *
     */
    public static void processInput(String[] inputLines) {
        int counter=0;
        busList = new ArrayList<>();
        boolean zReportRequired = false; // dosyanın sonunda z raporu yazmak için bayrak

        for (String line : inputLines) {
            counter++;
            String[] tokens = line.split("\t");
            String command = tokens[0];

            if (!command.equals("INIT_VOYAGE") && !command.equals("Z_REPORT") && !command.equals("SELL_TICKET") &&
                    !command.equals("REFUND_TICKET") && !command.equals("PRINT_VOYAGE") && !command.equals("CANCEL_VOYAGE")) {
                System.out.println("COMMAND: " + line);
                System.out.println("ERROR: There is no command namely " + command + "!");
                continue;
            }

            switch (command) {
                case "INIT_VOYAGE":
                    System.out.println("COMMAND: " + line);
                    makeInitVoyage(tokens);
                    zReportRequired = false;
                    break;

                case "Z_REPORT":
                    System.out.println("COMMAND: " + line);
                    printZReport(tokens, counter == inputLines.length);//if the counter is equals to length don't add a newline character
                    zReportRequired = true;
                    break;

                case "SELL_TICKET":
                    System.out.println("COMMAND: " + line);
                    processSellTicket(tokens);
                    zReportRequired = false;
                    break;

                case "REFUND_TICKET":
                    System.out.println("COMMAND: " + line);
                    RefundTicket(tokens);
                    zReportRequired = false;
                    break;

                case "PRINT_VOYAGE":
                    System.out.println("COMMAND: " + line);
                    printVoyage(tokens);
                    zReportRequired = false;
                    break;

                case "CANCEL_VOYAGE":
                    System.out.println("COMMAND: " + line);
                    CancelVoyage(tokens);
                    zReportRequired = false;
                    break;

                default:
                    System.out.println("Invalid command: " + command);
                    break;
            }
    }

    // if the last commend is not z report print z raport
    if (!zReportRequired ) {
        printFinalZReport(new String[0]);
    }
}


    /**
     * a method for INITVOYAGE command
     * @param tokens gives tokens true names and use them to create a voyage
     */
    private static void makeInitVoyage(String[] tokens) {
        if (tokens.length < 7 | tokens.length > 9) {
            System.out.println("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!");
            return;
        }
        String command = tokens[0];
        String type = tokens[1].trim();

        int id;
        try {
            id = Integer.parseInt(tokens[2]);
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: " + tokens[2]+ " is not a positive integer, ID of a voyage must be a positive integer!");
            return;
        }
        String from = tokens[3];
        String to = tokens[4];
        int rows;
        float price;
        try {
            rows = Integer.parseInt(tokens[5]);
            price = Float.parseFloat(tokens[6]);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Rows and price must be valid numbers!");
            return;
        }

        for (Bus bus : busList) {
            if (bus.getId() == id) {
                System.out.println("ERROR: There is already a voyage with ID of " + id + "!");
                return;
            }
        }

        if (rows <= 0) {
            System.out.println("ERROR: " + rows + " is not a positive integer, number of seat rows of a voyage must be a positive integer!");
            return;
        }
        if (price <= 0) {
            System.out.println("ERROR: " + Integer.parseInt(tokens[6]) + " is not a positive number, price must be a positive number!");
            return;
        }
        Bus bus;
        if (type.equals("Standard")) {
            int refundCut = Integer.parseInt(tokens[7]);
            if (refundCut < 0 | refundCut > 100) {
                System.out.println("ERROR: " + refundCut + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!");
                return;
            }
            System.out.printf("Voyage %d was initialized as a standard (2+2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that refunds will be %d%% less than the paid amount.\n", id, from, to, price, rows * 4, refundCut);
            bus = new Standard(command, type, id, from, to, rows, price, null, refundCut);
        } else if (type.equals("Premium")) {
            float refundCut = Float.parseFloat(tokens[7]);
            float premiumFee = Float.parseFloat(tokens[8]);
            float premiumPrice = price + (price * premiumFee / 100);
            if (refundCut < 0 | refundCut > 100) {
                System.out.println("ERROR: " + Integer.parseInt(tokens[7]) + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!");
                return;
            }
            if (premiumFee < 0 | premiumFee > 100) {
                System.out.println("ERROR: " + Integer.parseInt(tokens[8]) + " is not a non-negative integer, premium fee must be a non-negative integer!");
                return;
            }
            System.out.printf("Voyage %d was initialized as a premium (1+2) voyage from %s to %s with %.2f TL priced %d regular seats and %.2f TL priced %d premium seats. Note that refunds will be %d%% less than the paid amount.\n", id, from, to, price, rows * 2, premiumPrice, rows, (int) refundCut);


            Premium premiumBus = new Premium(command, type, id, from, to, rows, price, null, refundCut, premiumFee, premiumPrice);
            premiumBus.createSeatLayout();

            bus = premiumBus;
        } else if (type.equals("Minibus")) {
            if (tokens.length!=7){
                System.out.println("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!");
                return;
            }
            System.out.printf("Voyage %d was initialized as a minibus (2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that minibus tickets are not refundable.\n", id, from, to, price, rows * 2);
            bus = new Minibus(command, type, id, from, to, rows, price, null);
        } else {
            System.out.println("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!");
            return;
        }

        if (bus instanceof Standard) {
            ((Standard) bus).createSeatLayout();
        } else if (bus instanceof Premium) {
            ((Premium) bus).createSeatLayout();
        } else {
            ((Minibus) bus).createSeatLayout();
        }

        busList.add(bus);
        // ı have to sort bus list according to their id numbers because we are writing them sorted format
        busList.sort(Comparator.comparingInt(Bus::getId));
    }

    /**
     * a method for ZREPORT command
     * @param tokens gives tokens true names and use them to write z raport
     */
    private static void printZReport(String tokens[],boolean lastLine) {
        if (tokens.length > 1 | tokens.length < 1) {
            System.out.println("ERROR: Erroneous usage of \"Z_REPORT\" command!");
            return;
        }

        System.out.println("Z Report:");
        System.out.println("----------------");

        boolean anyVoyagesAvailable = false; // a flag to know if the voyage is cancelled or not

        int totalVoyageCount = busList.size();

        for (int i = 0; i < totalVoyageCount; i++) {
            Bus bus = busList.get(i);
            if (!bus.isCancelled()) {
                anyVoyagesAvailable = true;
                if (bus instanceof Standard) {
                    Standard.printBusMatrix((Standard) bus);
                } else if (bus instanceof Premium) {
                    Premium.printBusMatrix((Premium) bus);
                } else if (bus instanceof Minibus) {
                    Minibus.printBusMatrix((Minibus) bus);
                }
                if (i == totalVoyageCount - 1) {
                    System.out.println("Revenue: " + String.format("%.2f", bus.getRevenue()));
                    System.out.print("----------------");
                    if(!lastLine){
                        System.out.println();
                    }

               }
                else{ System.out.println("Revenue: " + String.format("%.2f", bus.getRevenue()));
                    System.out.println("----------------");



                }
            }
        }

        if (!anyVoyagesAvailable) {
            if (lastLine){
                System.out.println("No Voyages Available!");
                System.out.print("----------------");
            }

            else{
                System.out.println("No Voyages Available!");
                System.out.println("----------------");

            }
        }
    }

    /**
     * this method very similar to Print z report method but this method doesn't have error messages and there is no new line career
     */
    private static void printFinalZReport(String tokens[]) {

        System.out.println("Z Report:");
        System.out.println("----------------");

        boolean anyVoyagesAvailable = false;

        for (Bus bus : busList) {
            if (!bus.isCancelled()) {
                anyVoyagesAvailable = true;
                if (bus instanceof Standard) {
                    Standard.printBusMatrix((Standard) bus);
                } else if (bus instanceof Premium) {
                    Premium.printBusMatrix((Premium) bus);
                } else if (bus instanceof Minibus) {
                    Minibus.printBusMatrix((Minibus) bus);
                }
                System.out.println("Revenue: " + String.format("%.2f", bus.getRevenue()));

                System.out.println("----------------");
            }
        }
        if (!anyVoyagesAvailable) {
            System.out.println("No Voyages Available!");
            System.out.print("----------------");
        }
    }

    /**
     * a method for PRINTVOYAGE commend
     * it find the voyage according to its id and write its information
     */
    private static void printVoyage(String[] tokens) {
        if (tokens.length != 2) {
            System.out.println("ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!");
            return;
        }
        int voyageId;
        try {
            voyageId = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: " + tokens[1] + " is not a positive integer, ID of a voyage must be a positive integer!");
            return;
        }

        if (voyageId <= 0) {
            System.out.println("ERROR: " + voyageId + " is not a positive integer, ID of a voyage must be a positive integer!");
            return;
        }

        Bus targetBus = findBusById(voyageId);
        if (targetBus == null) {
            System.out.println("ERROR: There is no voyage with ID of " + voyageId + "!");
            return;
        }

        if (targetBus instanceof Standard) {
            Standard.printBusMatrix((Standard) targetBus);
        } else if (targetBus instanceof Premium) {
            Premium.printBusMatrix((Premium) targetBus);
        } else if (targetBus instanceof Minibus) {
            Minibus.printBusMatrix((Minibus) targetBus);
        }

        System.out.println("Revenue: " + String.format("%.2f", targetBus.getRevenue()));
    }

    /**
     * a methos for SELLTİCKET commend
     * @param tokens read  tokens as voyage id and seat numbers
     * it makes X the seats which be sold
     */
    private static void processSellTicket(String[] tokens) {
        if (tokens.length < 3) {
            System.out.println("ERROR: Erroneous usage of \"SELL_TICKET\" command!");
            return;
        }

        int voyageId;
        try {
            voyageId = Integer.parseInt(tokens[1]);
            if (voyageId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: " + tokens[1]+ " is not a positive integer, ID of a voyage must be a positive integer!");
            return;
        }
        String[] seatNumbersToken = tokens[2].split("_");
        List<Integer> seatNumbers = new ArrayList<>();
        for (String seatNumberToken : seatNumbersToken) {
            try {
                int seatNumber = Integer.parseInt(seatNumberToken);
                if (seatNumber <= 0) {
                    throw new NumberFormatException();
                }
                seatNumbers.add(seatNumber);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: " + seatNumberToken + " is not a positive integer, seat number must be a positive integer!");
                return;
            }
        }

        Bus targetBus = findBusById(voyageId);
        if (targetBus == null) {
            System.out.println("ERROR: There is no voyage with ID of " + voyageId + "!");
            return;
        }

        List<Integer> soldSeatNumbers = new ArrayList<>();
        float totalRevenue = 0;

        // check if the seat already sold
        for (String seatNumberToken : seatNumbersToken) {
            int seatNumber = Integer.parseInt(seatNumberToken);
            if (targetBus.isSeatSold(seatNumber)) {
                System.out.println("ERROR: One or more seats already sold!");
                return;
            }
        }
        for (String seatNumberToken : seatNumbersToken) {
            try {
                int seatNumber = Integer.parseInt(seatNumberToken);
                if (seatNumber <= 0) {
                    throw new NumberFormatException();
                }
                boolean success = targetBus.sellTicket(seatNumber);
                if (success) {
                    soldSeatNumbers.add(seatNumber);
                    // if the seat is premium it has different price so add this price
                    float seatPrice;
                    if (targetBus instanceof Premium && isPremiumSeat(seatNumber)) {
                        seatPrice = ((Premium) targetBus).getPremiumPrice();
                    } else {
                        seatPrice = targetBus.getPrice();
                    }
                    totalRevenue += seatPrice;
                } else {
                    System.out.println("ERROR: There is no such a seat!");
                    return;
                }
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("ERROR: " + seatNumberToken + " is not a positive integer, seat number must be a positive integer!");
                return;
            }
        }
        if (!soldSeatNumbers.isEmpty()) {
            System.out.print("Seat ");
            for (int i = 0; i < soldSeatNumbers.size(); i++) {
                System.out.print(soldSeatNumbers.get(i));
                if (i < soldSeatNumbers.size() - 1) {
                    System.out.print("-");
                }
            }
            System.out.printf(" of the Voyage %d from %s to %s was successfully sold for %.2f TL.\n", voyageId, targetBus.getFrom(), targetBus.getTo(), totalRevenue);            // Toplam geliri güncelle
            targetBus.setRevenue(targetBus.getRevenue() + totalRevenue);
        }
        soldSeatNumbers.clear();
    }

    /**
     *
     * @param seatNumber  is the seat premium or not
     * @return true if the seat is premium
     */
    private static boolean isPremiumSeat(int seatNumber) {
        return seatNumber % 3 == 1;
    }

    /**
     * a method for REFUNDTICKET commend
     * @param tokens read tokens as voyageid and refund seat numbers and if it is possible refund them and make them *
     */
    private static void RefundTicket(String[] tokens) {
        if (tokens.length != 3) {
            System.out.println("ERROR: Erroneous usage of \"REFUND_TICKET\" command!");
            return;
        }

        int voyageId = Integer.parseInt(tokens[1]);
        String[] seatNumbersToken = tokens[2].split("_");
        List<Integer> seatNumbers = new ArrayList<>();
        if (voyageId < 0) {
            System.out.println("ERROR: " + voyageId + " is not a positive integer, ID of a voyage must be a positive integer!");
            return;
        }
        Bus targetBus = findBusById(voyageId);
        if (targetBus == null) {
            System.out.println("ERROR: There is no voyage with ID of " + voyageId + "!");
            return;
        }
        boolean anySeatEmpty = false;
        for (String seatNumberToken : seatNumbersToken) {
            int seatNumber = Integer.parseInt(seatNumberToken);
            if (targetBus.isSeatEmpty(seatNumber)) {
                anySeatEmpty = true;
                break;
            }
        }
        if (anySeatEmpty) {
            System.out.println("ERROR: One or more seats are already empty!");
            return;
        }

        List<Integer> refundedSeatNumbers = new ArrayList<>();
        float totalRefundAmount = 0;

        for (String seatNumberToken : seatNumbersToken) {
            int seatNumber = Integer.parseInt(seatNumberToken);
            if (seatNumber < 0) {
                System.out.println("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!");
                return;
            }

            boolean success;

            if (targetBus instanceof Minibus) {
                System.out.println("ERROR: Minibus tickets are not refundable!");
                return;
            } else {
                success = targetBus.refundTicket(seatNumber);
            }
            //calculate the refund cut and add to revenue

            if (success) {
                refundedSeatNumbers.add(seatNumber);
                float refundCutPercentage = 0;
                float price;
                if (targetBus instanceof Standard) {
                    refundCutPercentage = ((Standard) targetBus).getRefundCut();
                    price = targetBus.getPrice();
                } else if (targetBus instanceof Premium) {
                    refundCutPercentage = ((Premium) targetBus).getRefundCut();
                    //if the seat is premium get primium price
                    price = isPremiumSeat(seatNumber) ? ((Premium) targetBus).getPremiumPrice() : targetBus.getPrice();
                } else {
                    price = ((Minibus) targetBus).getPrice();
                }
                float refundAmount = price * (1 - refundCutPercentage / 100);
                totalRefundAmount += refundAmount;
                targetBus.setRevenue(targetBus.getRevenue() - refundAmount);
            } else {
                System.out.println("ERROR: There is no such a seat!");
                return;
            }
        }
        if (!refundedSeatNumbers.isEmpty()) {
            System.out.print("Seat ");
            for (int i = 0; i < refundedSeatNumbers.size(); i++) {
                System.out.print(refundedSeatNumbers.get(i));
                if (i < refundedSeatNumbers.size() - 1) {
                    System.out.print("-");
                }
            }
            System.out.printf(" of the Voyage %d from %s to %s was successfully refunded for %.2f TL.\n", voyageId, targetBus.getFrom(), targetBus.getTo(), totalRefundAmount);
        }

    }

    /**
     *
     * @param voyageId find the bus which have this voyage id
     * @return the bus you found
     */
    private static Bus findBusById(int voyageId) {
        for (Bus bus : busList) {
            if (bus.getId() == voyageId) {
                return bus;
            }
        }
        return null;
    }

    /**
     * a methos for CancelVOYAGE commend
     * @param tokens read token as voyage id and cancel and delete this bus
     * calculate revenue with only the refund cut moneys
     */
    private static void CancelVoyage(String[] tokens) {
        if (tokens.length != 2) {
            System.out.println("ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!");
            return;
        }

        int voyageId = Integer.parseInt(tokens[1]);

        Bus targetBus = findBusById(voyageId);
        if (voyageId < 0) {
            System.out.println("ERROR: " + voyageId + " is not a positive integer, ID of a voyage must be a positive integer!");
            return;
        } else if (targetBus == null) {
            System.out.println("ERROR: There is no voyage with ID of " + voyageId + "!");
            return;
        }

        System.out.println("Voyage " + voyageId + " was successfully cancelled!");
        //write the last situation of bus
        System.out.println("Voyage details can be found below:");
        printVoyageCancel(targetBus);

        // calculate the revenue after cancel
        double revenue = targetBus.getRevenue();

        // calculate the number of sold and cancelled seats to calculate revenue
        int SeatCount = countSoldXShapedSeats(targetBus);
        float premiumSeatPrice=0;
        float cancelledRevenue=0;
        if (targetBus instanceof Premium) {
            int premiumSeat = 0;
            int rowCount = targetBus.getRows();
            int colCount = targetBus.getBusPlan()[0].length;

            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    if (targetBus.busPlan[i][j]== 'X') {
                        if ((j + 1) % 3 == 1) {
                            premiumSeat++;
                        }
                    }
                }
            }

            premiumSeatPrice = premiumSeat * ((Premium) targetBus).getPremiumPrice();
            cancelledRevenue = (SeatCount-premiumSeat) * targetBus.getPrice();
        }
        else{
            cancelledRevenue = SeatCount * targetBus.getPrice();
        }


        // after cancel delete the bus because we can use the id again
        busList.remove(targetBus);

        double totalRevenue = revenue - cancelledRevenue - premiumSeatPrice;
        System.out.println("Revenue: " + String.format("%.2f", totalRevenue));
    }
    private static int countSoldXShapedSeats(Bus targetBus) {
        int soldXShapedSeats = 0;

        int rowCount = targetBus.getRows();
        int colCount = targetBus.getBusPlan()[0].length;

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                if (targetBus.busPlan[i][j]== 'X' ){
                    soldXShapedSeats++;
                }
            }
        }

        return soldXShapedSeats;
    }

    private static void printVoyageCancel(Bus targetBus) {
        if (targetBus instanceof Standard) {
            Standard.printBusMatrix((Standard) targetBus);
        } else if (targetBus instanceof Premium) {
            Premium.printBusMatrix((Premium) targetBus);
        } else if (targetBus instanceof Minibus) {
            Minibus.printBusMatrix((Minibus) targetBus);
        }
    }
}



package pt.demanda.skyline;

public class BoardingPass implements java.io.Serializable {
    private static final long serialVersionUID = 8296009742803780051L;


    public String FormatCode;                         // [01] M
    public String NumberOfLegsEncoded;                // [01] 1
    public String PassengerName;                      // [20] RUI VARELA
    public String ElectronicTicketIndicator;          // [01]
    public String OperatingCarrierPNRCode;            // [07]
    public String FromCityAirportCode;                // [03]
    public String ToCityAirportCode;                  // [03]
    public String OperatingCarrierDesignator;         // [03]
    public String FlightNumber;                       // [05]
    public String DateOfFlightJulian;                 // [03]
    public String CompartmentCode;                    // [01]
    public String SeatNumber;                         // [04]
    public String CheckInSequenceNumber;              // [05]
    public String PassengerStatus;                    // [01]
    public String SizeOfConditionalPlusAirline;       // [02] 0

    public BoardingPass() {
        String barcode = "";
        barcode = "M1SARAIVA/CARLOS MR   E5XTVTP CPHFRALH 0831 301M030C0057 100";
        barcode = "M1NAME/DEFAULT MR     E5XTVTP CPHFRALH 0831 301M030C0057 100";
        fromString(barcode);
        //String computed = toString();
        //Log.d("BP" ,"Barcode: " + barcode);
        //Log.d("BP" ,"Compute: " + computed);
    }

    public boolean fromString(String raw) {
        boolean isOk = (raw != null) && (raw.length() >= 60);

        if (!isOk) return false;

        int current_index = 0;
        int size = 0;

        size = 1;
        FormatCode = raw.substring(current_index, current_index += size);

        size = 1;
        NumberOfLegsEncoded = raw.substring(current_index, current_index += size);

        size = 20;
        PassengerName = raw.substring(current_index, current_index += size).trim();

        size = 1;
        ElectronicTicketIndicator = raw.substring(current_index, current_index += size);

        size = 7;
        OperatingCarrierPNRCode = raw.substring(current_index, current_index += size).trim();

        size = 3;
        FromCityAirportCode = raw.substring(current_index, current_index += size).trim();

        size = 3;
        ToCityAirportCode = raw.substring(current_index, current_index += size).trim();

        size = 3;
        OperatingCarrierDesignator = raw.substring(current_index, current_index += size).trim();

        size = 5;
        FlightNumber = raw.substring(current_index, current_index += size).trim();

        size = 3;
        DateOfFlightJulian = raw.substring(current_index, current_index += size).trim();

        size = 1;
        CompartmentCode = raw.substring(current_index, current_index += size).trim();

        size = 4;
        SeatNumber = raw.substring(current_index, current_index += size).trim();

        size = 5;
        CheckInSequenceNumber = raw.substring(current_index, current_index += size).trim();

        size = 1;
        PassengerStatus = raw.substring(current_index, current_index += size).trim();

        size = 2;
        SizeOfConditionalPlusAirline = raw.substring(current_index, current_index += size).trim();

        return isOk;
    }

    public String Sanitize(String input, int size) {
        String sidePad = "";

        //if (!alignLeft)
        sidePad = "-";

        String output = String.format("%1$" + sidePad + size + "s", input);

        return output.substring(0, size);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(Sanitize(FormatCode, 1));
        builder.append(Sanitize(NumberOfLegsEncoded, 1));
        builder.append(Sanitize(PassengerName, 20));
        builder.append(Sanitize(ElectronicTicketIndicator, 1));
        builder.append(Sanitize(OperatingCarrierPNRCode, 7));
        builder.append(Sanitize(FromCityAirportCode, 3));
        builder.append(Sanitize(ToCityAirportCode, 3));
        builder.append(Sanitize(OperatingCarrierDesignator, 3));
        builder.append(Sanitize(FlightNumber, 5));
        builder.append(Sanitize(DateOfFlightJulian, 3));
        builder.append(Sanitize(CompartmentCode, 1));
        builder.append(Sanitize(SeatNumber, 4));
        builder.append(Sanitize(CheckInSequenceNumber, 5));
        builder.append(Sanitize(PassengerStatus, 1));
        builder.append(Sanitize(SizeOfConditionalPlusAirline, 2));

        return builder.toString();
    }
}

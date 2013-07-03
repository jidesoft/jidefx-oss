/*
 * @(#)DemoData.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.demo;

//import com.jidesoft.grid.DefaultContextSensitiveTableModel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class DemoData {

    public static final String PREFIX_SIGNUP_FORM = "SignUpForm";
    public static final String PREFIX_VALIDATION_FORM = "ValidationForm";

    public static ObservableList<String> createCountryList() {
        final String[] names = getCountryNames();
        return FXCollections.observableArrayList(names);
    }

    public static ObservableList<String> createStateList() {
        final String[] names = getStateNames();
        return FXCollections.observableArrayList(names);
    }

    public static class Country {
        StringProperty name;
        BooleanProperty selected;

        @Override
        public String toString() {
            return getName();
        }

        public StringProperty nameProperty() {
            if (name == null) {
                name = new SimpleStringProperty();
            }
            return name;
        }

        public String getName() {
            return name != null ? nameProperty().get() : "";
        }

        public void setName(String name) {
            nameProperty().set(name);
        }

        public BooleanProperty selectedProperty() {
            if (selected == null) {
                selected = new SimpleBooleanProperty();
            }
            return selected;
        }

        public boolean isSelected() {
            return selected != null ? selectedProperty().get() : false;
        }

        public void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

    }

    public static ObservableList<Country> createCountryObjectList() {
        final String[] names = getCountryNames();
        ObservableList<Country> countries = FXCollections.observableArrayList();
        for (String name : names) {
            Country e = new Country();
            e.setName(name);
            e.setSelected(false);
            countries.add(e);
        }
        return countries;
    }

    public static ObservableList<String> createFontList() {
        final String[] names = getFontNames();
        return FXCollections.observableArrayList(names);
    }

    public static TreeItem<String> createTaskTreeItem() {
        TreeItem<String> root = new TreeItem<>("Root");

        TreeItem<String> nodeItemA = new TreeItem<>("Item A");
        TreeItem<String> nodeItemB = new TreeItem<>("Item B");
        TreeItem<String> nodeItemC = new TreeItem<>("Item C");
        root.getChildren().addAll(nodeItemA, nodeItemB, nodeItemC);

        TreeItem<String> nodeItemA1 = new TreeItem<>("Item A1");
        TreeItem<String> nodeItemA2 = new TreeItem<>("Item A2");
        TreeItem<String> nodeItemA3 = new TreeItem<>("Item A3");
        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2, nodeItemA3);

        return root;
    }

    public static String[] getFontNames() {
        return new String[]{
                "Agency FB",
                "Aharoni",
                "Algerian",
                "Andalus",
                "Angsana New",
                "AngsanaUPC",
                "Aparajita",
                "Arabic Typesetting",
                "Arial",
                "Arial Black",
                "Arial Narrow",
                "Arial Rounded MT Bold",
                "Arial Unicode MS",
                "Baskerville Old Face",
                "Batang",
                "BatangChe",
                "Bauhaus 93",
                "Bell MT",
                "Berlin Sans FB",
                "Berlin Sans FB Demi",
                "Bernard MT Condensed",
                "Blackadder ITC",
                "Bodoni MT",
                "Bodoni MT Black",
                "Bodoni MT Condensed",
                "Bodoni MT Poster Compressed",
                "Book Antiqua",
                "Bookman Old Style",
                "Bookshelf Symbol 7",
                "Bradley Hand ITC",
                "Britannic Bold",
                "Broadway",
                "Browallia New",
                "BrowalliaUPC",
                "Brush Script MT",
                "Calibri",
                "Californian FB",
                "Calisto MT",
                "Cambria",
                "Cambria Math",
                "Candara",
                "Castellar",
                "Centaur",
                "Century",
                "Century Gothic",
                "Century Schoolbook",
                "Chiller",
                "Colonna MT",
                "Comic Sans MS",
                "Consolas",
                "Constantia",
                "Cooper Black",
                "Copperplate Gothic Bold",
                "Copperplate Gothic Light",
                "Corbel",
                "Cordia New",
                "CordiaUPC",
                "Courier New",
                "Curlz MT",
                "DaunPenh",
                "David",
                "DFKai-SB",
                "Dialog",
                "DialogInput",
                "DilleniaUPC",
                "DokChampa",
                "Dotum",
                "DotumChe",
                "Ebrima",
                "Edwardian Script ITC",
                "Elephant",
                "Engravers MT",
                "Estrangelo Edessa",
                "EucrosiaUPC",
                "Euphemia",
                "Euro Sign",
                "FangSong",
                "Felix Titling",
                "Footlight MT Light",
                "Forte",
                "Franklin Gothic Book",
                "Franklin Gothic Demi",
                "Franklin Gothic Demi Cond",
                "Franklin Gothic Heavy",
                "Franklin Gothic Medium",
                "Franklin Gothic Medium Cond",
                "FrankRuehl",
                "FreesiaUPC",
                "Freestyle Script",
                "French Script MT",
                "Gabriola",
                "Garamond",
                "Gautami",
                "Georgia",
                "Gigi",
                "Gill Sans MT",
                "Gill Sans MT Condensed",
                "Gill Sans MT Ext Condensed Bold",
                "Gill Sans Ultra Bold",
                "Gill Sans Ultra Bold Condensed",
                "Gisha",
                "Gloucester MT Extra Condensed",
                "Goudy Old Style",
                "Goudy Stout",
                "Gulim",
                "GulimChe",
                "Gungsuh",
                "GungsuhChe",
                "Haettenschweiler",
                "Harlow Solid Italic",
                "Harrington",
                "High Tower Text",
                "Impact",
                "Imprint MT Shadow",
                "Informal Roman",
                "IrisUPC",
                "Iskoola Pota",
                "JasmineUPC",
                "Jokerman",
                "Juice ITC",
                "KaiTi",
                "Kalinga",
                "Kartika",
                "Khmer UI",
                "KodchiangUPC",
                "Kokila",
                "Kristen ITC",
                "Kunstler Script",
                "Lao UI",
                "Latha",
                "Leelawadee",
                "Levenim MT",
                "LilyUPC",
                "Lucida Bright",
                "Lucida Calligraphy",
                "Lucida Console",
                "Lucida Fax",
                "Lucida Handwriting",
                "Lucida Sans",
                "Lucida Sans Typewriter",
                "Lucida Sans Unicode",
                "Magneto",
                "Maiandra GD",
                "Malgun Gothic",
                "Mangal",
                "Marlett",
                "Matura MT Script Capitals",
                "Meiryo",
                "Meiryo UI",
                "Microsoft Himalaya",
                "Microsoft JhengHei",
                "Microsoft New Tai Lue",
                "Microsoft PhagsPa",
                "Microsoft Sans Serif",
                "Microsoft Tai Le",
                "Microsoft Uighur",
                "Microsoft YaHei",
                "Microsoft Yi Baiti",
                "MingLiU",
                "MingLiU-ExtB",
                "MingLiU_HKSCS",
                "MingLiU_HKSCS-ExtB",
                "Miriam",
                "Miriam Fixed",
                "Mistral",
                "Modern No. 20",
                "Mongolian Baiti",
                "Monospaced",
                "Monotype Corsiva",
                "MoolBoran",
                "MS Gothic",
                "MS Mincho",
                "MS Outlook",
                "MS PGothic",
                "MS PMincho",
                "MS Reference Sans Serif",
                "MS Reference Specialty",
                "MS UI Gothic",
                "MT Extra",
                "MV Boli",
                "Narkisim",
                "Niagara Engraved",
                "Niagara Solid",
                "Niamey",
                "NSimSun",
                "Nyala",
                "OCR A Extended",
                "OCR B MT",
                "OCR-A II",
                "Old English Text MT",
                "Onyx",
                "Palace Script MT",
                "Palatino Linotype",
                "Papyrus",
                "Parchment",
                "Perpetua",
                "Perpetua Titling MT",
                "Plantagenet Cherokee",
                "Playbill",
                "PMingLiU",
                "PMingLiU-ExtB",
                "Poor Richard",
                "Pristina",
                "QuickType",
                "QuickType Condensed",
                "QuickType II",
                "QuickType II Condensed",
                "QuickType II Mono",
                "QuickType II Pi",
                "QuickType Mono",
                "QuickType Pi",
                "Raavi",
                "Rage Italic",
                "Ravie",
                "Rockwell",
                "Rockwell Condensed",
                "Rockwell Extra Bold",
                "Rod",
                "Sakkal Majalla",
                "SansSerif",
                "Script MT Bold",
                "Segoe Print",
                "Segoe Script",
                "Segoe UI",
                "Segoe UI Light",
                "Segoe UI Semibold",
                "Segoe UI Symbol",
                "Serif",
                "Shonar Bangla",
                "Showcard Gothic",
                "Shruti",
                "SimHei",
                "Simplified Arabic",
                "Simplified Arabic Fixed",
                "SimSun",
                "SimSun-ExtB",
                "Snap ITC",
                "Stencil",
                "SWGamekeys MT",
                "Sylfaen",
                "Symbol",
                "Tahoma",
                "Tempus Sans ITC",
                "Times New Roman",
                "Traditional Arabic",
                "Trebuchet MS",
                "Tunga",
                "Tw Cen MT",
                "Tw Cen MT Condensed",
                "Tw Cen MT Condensed Extra Bold",
                "Untitled",
                "Utsaah",
                "Vani",
                "Verdana",
                "Vijaya",
                "Viner Hand ITC",
                "Vivaldi",
                "Vladimir Script",
                "Vodafone Rg",
                "Vrinda",
                "Webdings",
                "Wide Latin",
                "Wingdings",
                "Wingdings 2",
                "Wingdings 3",
                "ZWAdobeF",
        };
    }

    public static final String[] getStateNames() {
        return new String[]{
                "Alabama",
                "Alaska",
                "Arizona",
                "Arkansas",
                "California",
                "Colorado",
                "Connecticut",
                "Delaware",
                "Florida",
                "Georgia",
                "Hawaii",
                "Idaho",
                "Illinois",
                "Indiana",
                "Iowa",
                "Kansas",
                "Kentucky",
                "Louisiana",
                "Maine",
                "Maryland",
                "Massachusetts",
                "Michigan",
                "Minnesota",
                "Mississippi",
                "Missouri",
                "Montana",
                "Nebraska",
                "Nevada",
                "New Hampshire",
                "New Jersey	 ",
                "New Mexico",
                "New York",
                "North Carolina",
                "North Dakota",
                "Ohio",
                "Oklahoma",
                "Oregon",
                "Pennsylvania",
                "Rhode Island",
                "South Carolina",
                "South Dakota",
                "Tennessee",
                "Texas",
                "Utah",
                "Vermont",
                "Virginia",
                "Washington",
                "West Virginia",
                "Wisconsin",
                "Wyoming"
        };
    }

    public static String[] getCountryNames() {
        return new String[]{
                "Andorra",
                "United Arab Emirates",
                "Afghanistan",
                "Antigua And Barbuda",
                "Anguilla",
                "Albania",
                "Armenia",
                "Netherlands Antilles",
                "Angola",
                "Antarctica",
                "Argentina",
                "American Samoa",
                "Austria",
                "Australia",
                "Aruba",
                "Azerbaijan",
                "Bosnia And Herzegovina",
                "Barbados",
                "Bangladesh",
                "Belgium",
                "Burkina Faso",
                "Bulgaria",
                "Bahrain",
                "Burundi",
                "Benin",
                "Bermuda",
                "Brunei Darussalam",
                "Bolivia",
                "Brazil",
                "Bahamas",
                "Bhutan",
                "Bouvet Island",
                "Botswana",
                "Belarus",
                "Belize",
                "Canada",
                "Cocos (Keeling) Islands",
                "Congo, The Democratic Republic Of The",
                "Central African Republic",
                "Congo",
                "Switzerland",
                "Cook Islands",
                "Chile",
                "Cameroon",
                "China",
                "Colombia",
                "Costa Rica",
                "Cuba",
                "Cape Verde",
                "Christmas Island",
                "Cyprus",
                "Czech Republic",
                "Germany",
                "Djibouti",
                "Denmark",
                "Dominica",
                "Dominican Republic",
                "Algeria",
                "Ecuador",
                "Estonia",
                "Egypt",
                "Western Sarara",
                "Eritrea",
                "Spain",
                "Ethiopia",
                "Finland",
                "Fiji",
                "Falkland Islands (Malvinas)",
                "Micronesia, Federated States Of",
                "Faroe Islands",
                "France",
                "Gabon",
                "United Kingdom",
                "Grenada",
                "Georgia",
                "French Guiana",
                "Ghana",
                "Gibraltar",
                "Greenland",
                "Gambia",
                "Guinea",
                "Guadeloupe",
                "Equatorial Guinea",
                "Greece",
                "South Georgia And The South Sandwich Islands",
                "Guatemala",
                "Guam",
                "Guinea-bissau",
                "Guyana",
                "Hong Kong",
                "Heard Island And Mcdonald Islands",
                "Honduras",
                "Croatia",
                "Haiti",
                "Hungary",
                "Indonesia",
                "Ireland",
                "Israel",
                "India",
                "British Indian Ocean Territory",
                "Iraq",
                "Iran, Islamic Republic Of",
                "Iceland",
                "Italy",
                "Jamaica",
                "Jordan",
                "Japan",
                "Kenya",
                "Kyrgyzstan",
                "Cambodia",
                "Kiribati",
                "Comoros",
                "Saint Kitts And Nevis",
                "Korea, Democratic People'S Republic Of",
                "Korea, Republic Of",
                "Kuwait",
                "Cayman Islands",
                "Kazakhstan",
                "Lao People'S Democratic Republic",
                "Lebanon",
                "Saint Lucia",
                "Liechtenstein",
                "Sri Lanka",
                "Liberia",
                "Lesotho",
                "Lithuania",
                "Luxembourg",
                "Latvia",
                "Libyan Arab Jamabiriya",
                "Morocco",
                "Monaco",
                "Moldova, Republic Of",
                "Madagascar",
                "Marshall Islands",
                "Macedonia, The Former Yugoslav Republic Of",
                "Mali",
                "Myanmar",
                "Mongolia",
                "Macau",
                "Northern Mariana Islands",
                "Martinique",
                "Mauritania",
                "Montserrat",
                "Malta",
                "Mauritius",
                "Maldives",
                "Malawi",
                "Mexico",
                "Malaysia",
                "Mozambique",
                "Namibia",
                "New Caledonia",
                "Niger",
                "Norfolk Island",
                "Nigeria",
                "Nicaragua",
                "Netherlands",
                "Norway",
                "Nepal",
                "Niue",
                "New Zealand",
                "Oman",
                "Panama",
                "Peru",
                "French Polynesia",
                "Papua New Guinea",
                "Philippines",
                "Pakistan",
                "Poland",
                "Saint Pierre And Miquelon",
                "Pitcairn",
                "Puerto Rico",
                "Portugal",
                "Palau",
                "Paraguay",
                "Qatar",
                "Romania",
                "Russian Federation",
                "Rwanda",
                "Saudi Arabia",
                "Solomon Islands",
                "Seychelles",
                "Sudan",
                "Sweden",
                "Singapore",
                "Saint Helena",
                "Slovenia",
                "Svalbard And Jan Mayen",
                "Slovakia",
                "Sierra Leone",
                "San Marino",
                "Senegal",
                "Somalia",
                "Suriname",
                "Sao Tome And Principe",
                "El Salvador",
                "Syrian Arab Republic",
                "Swaziland",
                "Turks And Caicos Islands",
                "Chad",
                "French Southern Territories",
                "Togo",
                "Thailand",
                "Tajikistan",
                "Tokelau",
                "Turkmenistan",
                "Tunisia",
                "Tonga",
                "East Timor",
                "Turkey",
                "Trinidad And Tobago",
                "Tuvalu",
                "Taiwan, Province Of China",
                "Tanzania, United Republic Of",
                "Ukraine",
                "Uganda",
                "United States Minor Outlying Islands",
                "United States",
                "Uruguay",
                "Uzbekistan",
                "Venezuela",
                "Virgin Islands, British",
                "Virgin Islands, U.S.",
                "Viet Nam",
                "Vanuatu",
                "Wallis And Futuna",
                "Samoa",
                "Yemen",
                "Mayotte",
                "Yugoslavia",
                "South Africa",
                "Zambia",
                "Zimbabwe"
        };
    }

    public static ListModel<String> createCountryListModel() {
        final String[] names = getCountryNames();
        final DefaultListModel<String> model = new DefaultListModel<>();
        for (String name : names) {
            model.addElement(name);
        }
        return model;
    }

    public static ComboBoxModel<String> createCountryComboBoxModel() {
        final String[] names = getCountryNames();
        final DefaultComboBoxModel model = new DefaultComboBoxModel<String>();
        for (String name : names) {
            model.addElement(name);
        }
        return model;
    }

    public static ObservableList<ProductSales> createProductReports(int repeats, int maxRows) {
        try {
            InputStream resource = DemoData.class.getResourceAsStream("ProductReports.txt.gz");
            if (resource == null) {
                return null;
            }
            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            ObservableList<ProductSales> data = FXCollections.observableArrayList();
            Vector<String> columnNames = new Vector<>();

            String columnsLine = reader.readLine(); // skip first line
            String[] columnValues = columnsLine.split("\t");
            columnNames.addAll(Arrays.asList(columnValues));

            int count = 0;
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                ProductSales record = new ProductSales();
                record.setCategoryName(values[0]); // category  name
                record.setProductName(values[1]); // product name
                {
                    String value = values[2];
                    if (value.startsWith("$")) {
                        record.setAmount(Float.parseFloat(value.substring(1))); // product amount
                    }
                }
                {
                    String value = values[3];
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                        record.setDate(format.parse(value)); // order date
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < repeats; i++) {
                    data.add(record);
                }
                count++;
                if (maxRows > 0 && count > maxRows) {
                    break;
                }
            }
            while (true);
            return data;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ProductSales {
        private StringProperty categoryName;
        private StringProperty productName;
        private FloatProperty amount;
        private ObjectProperty<Date> date;

        public ProductSales() {
            categoryName = new SimpleStringProperty();
            productName = new SimpleStringProperty();
            amount = new SimpleFloatProperty();
            date = new SimpleObjectProperty<>();
        }

        public StringProperty categoryNameProperty() {
            return categoryName;
        }

        public StringProperty productNameProperty() {
            return productName;
        }

        public FloatProperty amountProperty() {
            return amount;
        }

        public ObjectProperty<Date> dateProperty() {
            return date;
        }

        public String getCategoryName() {
            return categoryNameProperty().get();
        }

        public void setCategoryName(String name) {
            categoryNameProperty().set(name);
        }

        public String getProductName() {
            return productNameProperty().get();
        }

        public void setProductName(String name) {
            productNameProperty().set(name);
        }

        public float getAmount() {
            return amountProperty().get();
        }

        public void setAmount(float value) {
            amountProperty().set(value);
        }

        public Date getDate() {
            return dateProperty().get();
        }

        public void setDate(Date value) {
            dateProperty().set(value);
        }
    }

    static HashMap<String, Object> map = new HashMap<>();

    static {
        map.put("Bounds", new Rectangle(0, 0, 100, 200));
        map.put("Background", new Color(1, 0, 0, 0));
        map.put("Foreground", new Color(1, 1, 1, 0));
        map.put("File Name", "C:\\Sample.java");
        map.put("Folder Name", "C:\\");
        map.put("CreationDate", Calendar.getInstance());
        map.put("ExpirationDate", Calendar.getInstance());
        map.put("DateTime", Calendar.getInstance());
        map.put("Name", "Label1");
        map.put("Font Name", "Arial");
        map.put("Font", new Font("Tahoma", 11));
        map.put("Default Font Name", "Verdana");
        map.put("Text", "Data");
        map.put("Opaque", Boolean.FALSE);
        map.put("Visible", Boolean.TRUE);
        map.put("Not Editable", 10);
        map.put("Long", (long) 123456789);
        map.put("Integer", 1234);
        map.put("Slider", 50);
        map.put("Double", 1.234567890);
        map.put("Float", new Float(0.01));
        map.put("Calculator", 0.0);
        map.put("SSN", "000000000");
        map.put("IP Address", "192.168.0.1");
        map.put("Priority", 1);
        map.put("Tristate", 2);
        map.put("Multiline", "This is a multiple line cell editor. \nA new line starts here.");
        map.put("Password", new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'});
        map.put("Multiple Values", new String[]{"A", "B", "C"});
    }


    public static class Song {
        public Song() {
        }

        public Song(String name, String artist, String album, String genre, String time, int year, Color mood, Date created) {
            setName(name);
            setArtist(artist);
            setAlbum(album);
            setGenre(genre);
            setTime(time);
            setYear(year);
            setMood(mood);
            setCreated(created);
        }

        private StringProperty _nameProperty;

        public StringProperty nameProperty() {
            if (_nameProperty == null) {
                _nameProperty = new SimpleStringProperty();
            }
            return _nameProperty;
        }

        public String getName() {
            return nameProperty().get();
        }

        public void setName(String nameProperty) {
            nameProperty().set(nameProperty);
        }

        private StringProperty _artistProperty;

        public StringProperty artistProperty() {
            if (_artistProperty == null) {
                _artistProperty = new SimpleStringProperty();
            }
            return _artistProperty;
        }

        public String getArtist() {
            return artistProperty().get();
        }

        public void setArtist(String artistProperty) {
            artistProperty().set(artistProperty);
        }


        private StringProperty _albumProperty;

        public StringProperty albumProperty() {
            if (_albumProperty == null) {
                _albumProperty = new SimpleStringProperty();
            }
            return _albumProperty;
        }

        public String getAlbum() {
            return albumProperty().get();
        }

        public void setAlbum(String album) {
            albumProperty().set(album);
        }

        private StringProperty _genreProperty;

        public StringProperty genreProperty() {
            if (_genreProperty == null) {
                _genreProperty = new SimpleStringProperty();
            }
            return _genreProperty;
        }

        public String getGenre() {
            return genreProperty().get();
        }

        public void setGenre(String genrePProperty) {
            genreProperty().set(genrePProperty);
        }

        private StringProperty _timeProperty;

        public StringProperty timeProperty() {
            if (_timeProperty == null) {
                _timeProperty = new SimpleStringProperty();
            }
            return _timeProperty;
        }

        public String getTime() {
            return timeProperty().get();
        }

        public void setTime(String time) {
            timeProperty().set(time);
        }

        private IntegerProperty _yearProperty;

        public IntegerProperty yearProperty() {
            if (_yearProperty == null) {
                _yearProperty = new SimpleIntegerProperty();
            }
            return _yearProperty;
        }

        public int getYear() {
            return yearProperty().get();
        }

        public void setYear(int year) {
            yearProperty().set(year);
        }

        @Override
        public String toString() {
            return getName();
        }

        private ObjectProperty<Color> _moodProperty;

        public ObjectProperty<Color> moodProperty() {
            if (_moodProperty == null) {
                _moodProperty = new SimpleObjectProperty<>();
            }
            return _moodProperty;
        }

        public Color getMood() {
            return moodProperty().get();
        }

        public void setMood(Color mood) {
            moodProperty().set(mood);
        }

        private ObjectProperty<Date> _createdProperty;

        public ObjectProperty<Date> createdProperty() {
            if (_createdProperty == null) {
                _createdProperty = new SimpleObjectProperty<>();
            }
            return _createdProperty;
        }

        public Date getCreated() {
            return createdProperty().get();
        }

        public void setCreated(Date created) {
            createdProperty().set(created);
        }
    }

    public static TableModel createSongTableModel() {
        try {
            InputStream resource = DemoData.class.getResourceAsStream("Library.txt.gz");
            if (resource == null) {
                return null;
            }
            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Vector<Vector<Object>> data = new Vector<>();
            Vector<String> columnNames = new Vector<>();

            String columnsLine = reader.readLine(); // skip first line
            String[] columnValues = columnsLine.split("\t");
            columnNames.addAll(Arrays.asList(columnValues));
            columnNames.add("Color");
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                Vector<Object> lineData = new Vector<>();
                if (values.length < 1)
                    lineData.add(null); // song name
                else
                    lineData.add(values[0]); // song name
                if (values.length < 2)
                    lineData.add(null); // artist
                else
                    lineData.add(values[1]); // artist
                if (values.length < 3)
                    lineData.add(null); // album
                else
                    lineData.add(values[2]); // album
                if (values.length < 4)
                    lineData.add(null); // genre
                else
                    lineData.add(values[3]); // genre
                if (values.length < 5)
                    lineData.add(null); // time
                else
                    lineData.add(values[4]); // time
                if (values.length < 6)
                    lineData.add(null); // year
                else
                    lineData.add(values[5]); // year

                lineData.add(Color.RED);
                data.add(lineData);

            }
            while (true);
            return null;
//            return new DefaultContextSensitiveTableModel(data, columnNames) {
//                @Override
//                public Class<?> getColumnClass(int column) {
//                    return column != getColumnCount() - 1 ? String.class : Color.class;
//                }
//            };
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<Song> createSongList(int max) {
        try {
            InputStream resource = DemoData.class.getResourceAsStream("Library.txt.gz");
            if (resource == null) {
                return null;
            }
            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            ObservableList<Song> data = FXCollections.<Song>observableArrayList();
            Vector<String> columnNames = new Vector<>();

            String columnsLine = reader.readLine(); // skip first line
            String[] columnValues = columnsLine.split("\t");
            columnNames.addAll(Arrays.asList(columnValues));
            columnNames.add("Mood");

            int n = 0;
            do {
                if (max >= 0 && max <= n) {
                    break;
                }
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                Song song = new Song();
                if (values.length >= 1) {
                    song.setName(values[0]); // song name
                }

                if (values.length >= 2) {
                    song.setArtist(values[1]); // artist
                }

                if (values.length >= 3) {
                    song.setAlbum(values[2]); // album
                }

                if (values.length >= 4) {
                    song.setGenre(values[3]); // genre
                }

                if (values.length >= 5) {
                    song.setTime(values[4]); // time
                }

                if (values.length >= 6) {
                    try {
                        song.setYear(Integer.parseInt(values[5])); // year
                    }
                    catch (NumberFormatException e) {
                        // ignore
                    }
                }

                song.setMood(Color.TRANSPARENT);
                song.setCreated(new Date());

                n++;
                data.add(song);
            }
            while (true);
            return data;
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<Song> createSongList() {
        return createSongList(-1);
    }

    public static List<String> readUrls() throws IOException {
        InputStream resource = DemoData.class.getResourceAsStream("url.txt.gz");
        if (resource == null) {
            return null;
        }

        InputStream in = new GZIPInputStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        List<String> list = new ArrayList<>();
        while (true) {
            String line = reader.readLine(); // skip first line
            if (line == null || line.length() == 0) {
                break;
            }
            list.add(line);
        }
        return list;
    }

    public static Region createSignUpForm() {

        MigPane pane = new MigPane(new LC().width("450px").height("240px").insets("20 10 10 10"), new AC().index(0).align("right").gap("16px").index(1).grow(), new AC().gap("6px"));

        Label emailLabel = new Label("Your Email");
        emailLabel.setId(PREFIX_SIGNUP_FORM + "emailLabel");
        TextField emailField = new TextField();
        emailField.setId(PREFIX_SIGNUP_FORM + "emailField");

        Label confirmEmailLabel = new Label("Confirm Email");
        confirmEmailLabel.setId(PREFIX_SIGNUP_FORM + "confirmEmailLabel");
        TextField confirmEmailField = new TextField();
        confirmEmailField.setId(PREFIX_SIGNUP_FORM + "confirmEmailField");

        Label countryLabel = new Label("Country");
        ChoiceBox<String> countryChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("United States", "Canada", "Mexico"));
        countryChoiceBox.setId(PREFIX_SIGNUP_FORM + "countryChoiceBox");

        Label zipCodeLabel = new Label("Zip Code");
        TextField zipCodeField = new TextField();
        zipCodeField.setId(PREFIX_SIGNUP_FORM + "zipCodeField");

        Label passwordLabel = new Label("Password");
        PasswordField passwordField = new PasswordField();
        Label confirmPasswordLabel = new Label("Confirm Password");
        passwordField.setId(PREFIX_SIGNUP_FORM + "passwordField");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setId(PREFIX_SIGNUP_FORM + "confirmPasswordField");

        CheckBox agreeCheckBox = new CheckBox("Yes, I agree to the term of use");
        agreeCheckBox.setId(PREFIX_SIGNUP_FORM + "agreeCheckBox");

        Button signUpButton = new Button("Sign Up");
        signUpButton.setId(PREFIX_SIGNUP_FORM + "signUpButton");
        signUpButton.disableProperty().bind(agreeCheckBox.selectedProperty().not());

        pane.add(emailLabel);
        pane.add(emailField, new CC().width("250px").wrap());

        pane.add(confirmEmailLabel);
        pane.add(confirmEmailField, new CC().width("250px").wrap());

        pane.add(countryLabel);
        pane.add(countryChoiceBox, new CC().width("250px").wrap());
        pane.add(zipCodeLabel);
        pane.add(zipCodeField, new CC().maxWidth("80px").wrap());
        pane.add(passwordLabel);
        pane.add(passwordField, new CC().width("250px").wrap());
        pane.add(confirmPasswordLabel);
        pane.add(confirmPasswordField, new CC().width("250px").wrap());
        pane.add(new Label(""));
        pane.add(agreeCheckBox, new CC().wrap("20px"));
        pane.add(new Label(""));
        pane.add(signUpButton, new CC().split().gapRight("10px").wrap());

        return pane;
    }

    public static Region createValidationForm() {

        MigPane pane = new MigPane(new LC().width("450px").height("350px").insets("20 10 10 10"), new AC().index(0).align("right").gap("10px").index(1).fill().grow().gap("6px").fill(), new AC().gap("6px"));

        Label emailLabel = new Label("Email:");
        emailLabel.setId(PREFIX_VALIDATION_FORM + "emailLabel");
        TextField emailField = new TextField("");
        emailField.setId(PREFIX_VALIDATION_FORM + "emailField");
        pane.add(emailLabel);
        pane.add(emailField, new CC().wrap());

        Label urlLabel = new Label("URL:");
        urlLabel.setId(PREFIX_VALIDATION_FORM + "urlLabel");
        TextField urlField = new TextField("");
        urlField.setId(PREFIX_VALIDATION_FORM + "urlField");
        pane.add(urlLabel);
        pane.add(urlField, new CC());
        Button urlButton = new Button("Open", new ImageView(new Image("/jidefx/examples/decoration/web.png")));
        urlButton.setId(PREFIX_VALIDATION_FORM + "urlButton");
        urlButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(urlField.getText()));
                }
                catch (Exception e) {
                    // ignore
                }
            }
        });
        urlButton.setDisable(true);
        pane.add(urlButton, new CC().wrap());

        Label ISBNLabel = new Label("ISBN:");
        ISBNLabel.setId(PREFIX_VALIDATION_FORM + "ISBNLabel");
        TextField ISBNField = new TextField("");
        ISBNField.setId(PREFIX_VALIDATION_FORM + "ISBNField");
        pane.add(ISBNLabel);
        pane.add(ISBNField, new CC());
        Button amazonButton = new Button("Amazon", new ImageView(new Image("/jidefx/examples/decoration/Amazon.png")));
        amazonButton.setId(PREFIX_VALIDATION_FORM + "amazonButton");
        amazonButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI("http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Dstripbooks&field-keywords=" + ISBNField.getText()));
                }
                catch (Exception e) {
                    // ignore
                }
            }
        });
        amazonButton.setDisable(true);
        pane.add(amazonButton, new CC().wrap());

        Label IP4Label = new Label("IP4:");
        IP4Label.setId(PREFIX_VALIDATION_FORM + "IP4Label");
        TextField IP4Field = new TextField("");
        IP4Field.setId(PREFIX_VALIDATION_FORM + "IP4Field");
        pane.add(IP4Label);
        pane.add(IP4Field, new CC().wrap());

        Label intLabel = new Label("Integer:");
        intLabel.setId(PREFIX_VALIDATION_FORM + "intLabel");
        TextField intField = new TextField("");
        intField.setId(PREFIX_VALIDATION_FORM + "intField");
        pane.add(intLabel);
        pane.add(intField, new CC().wrap());

        Label doubleLabel = new Label("Double:");
        doubleLabel.setId(PREFIX_VALIDATION_FORM + "doubleLabel");
        TextField doubleField = new TextField("");
        doubleField.setId(PREFIX_VALIDATION_FORM + "doubleField");
        pane.add(doubleLabel);
        pane.add(doubleField, new CC().wrap());

        Label currencyLabel = new Label("Currency:");
        currencyLabel.setId(PREFIX_VALIDATION_FORM + "currencyLabel");
        TextField currencyField = new TextField("");
        currencyField.setId(PREFIX_VALIDATION_FORM + "currencyField");
        pane.add(currencyLabel);
        pane.add(currencyField, new CC().wrap());

        Label percentLabel = new Label("Percent:");
        percentLabel.setId(PREFIX_VALIDATION_FORM + "percentLabel");
        TextField percentField = new TextField("");
        percentField.setId(PREFIX_VALIDATION_FORM + "percentField");
        pane.add(percentLabel);
        pane.add(percentField, new CC().wrap());

        Label dateLabel = new Label("Date:");
        dateLabel.setId(PREFIX_VALIDATION_FORM + "dateLabel");
        TextField dateField = new TextField("");
        dateField.setId(PREFIX_VALIDATION_FORM + "dateField");
        pane.add(dateLabel);
        pane.add(dateField, new CC().wrap());

        Label cardLabel = new Label("Credit Card:");
        cardLabel.setId(PREFIX_VALIDATION_FORM + "cardLabel");
        TextField cardField = new TextField("");
        cardField.setId(PREFIX_VALIDATION_FORM + "cardField");
        pane.add(cardLabel);
        pane.add(cardField, new CC());
        ImageView cardImage = new ImageView();
        cardImage.setId(PREFIX_VALIDATION_FORM + "cardImage");
        pane.add(cardImage, new CC().wrap());

        return pane;
    }

}

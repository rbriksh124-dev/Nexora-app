package com.example.data.catalog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.data.models.ToolCategory
import com.example.data.models.UtilityTool

object ToolCatalog {
    val tools: List<UtilityTool> = listOf(
        // Calculators
        UtilityTool(
            id = "calc_standard",
            nameEn = "Standard Calculator",
            nameHi = "मानक कैलकुलेटर",
            descriptionEn = "Basic arithmetic operations with clean history and memory",
            descriptionHi = "बुनियादी अंकगणितीय गणना और इतिहास",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Calculate,
            keywords = listOf("math", "add", "subtract", "divide", "multiply"),
            isPopular = true,
            isFeatured = true
        ),
        UtilityTool(
            id = "calc_scientific",
            nameEn = "Scientific Calculator",
            nameHi = "वैज्ञानिक कैलकुलेटर",
            descriptionEn = "Trigonometric, logarithmic, exponential, powers and factorial",
            descriptionHi = "त्रिकोणमिति, लघुगणक, घात और क्रमगुणित",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Functions,
            keywords = listOf("sin", "cos", "tan", "log", "pi", "e", "power"),
            isPopular = true
        ),
        UtilityTool(
            id = "calc_percentage",
            nameEn = "Percentage Calculator",
            nameHi = "प्रतिशत कैलकुलेटर",
            descriptionEn = "Calculate X% of Y, increase/decrease, and ratio percentage",
            descriptionHi = "प्रतिशत मान, वृद्धि अथवा कमी की गणना",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Percent,
            keywords = listOf("percent", "ratio", "portion", "tax"),
            isPopular = true
        ),
        UtilityTool(
            id = "calc_discount",
            nameEn = "Discount Calculator",
            nameHi = "छूट कैलकुलेटर",
            descriptionEn = "Original price, discount percentage, savings, and final price",
            descriptionHi = "मूल मूल्य, छूट प्रतिशत, बचत और अंतिम मूल्य",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Loyalty,
            keywords = listOf("sale", "discount", "offer", "savings", "shopping")
        ),
        UtilityTool(
            id = "calc_gst",
            nameEn = "GST Calculator",
            nameHi = "जीएसटी कैलकुलेटर",
            descriptionEn = "Indian GST (5%, 12%, 18%, 28%) with CGST, SGST, IGST breakdown",
            descriptionHi = "सीजीएसटी, एसजीएसटी, आईजीएसटी सहित भारतीय जीएसटी",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.ReceiptLong,
            keywords = listOf("gst", "tax", "cgst", "sgst", "igst", "india"),
            isPopular = true
        ),
        UtilityTool(
            id = "calc_emi",
            nameEn = "EMI Calculator",
            nameHi = "ईएमआई कैलकुलेटर",
            descriptionEn = "Equated monthly installments, total interest, and amortized payable",
            descriptionHi = "मासिक किस्त, कुल ब्याज और देय राशि",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.AccountBalance,
            keywords = listOf("loan", "emi", "interest", "bank", "finance"),
            isPopular = true,
            isFeatured = true
        ),
        UtilityTool(
            id = "calc_simple_interest",
            nameEn = "Simple Interest Calculator",
            nameHi = "साधारण ब्याज कैलकुलेटर",
            descriptionEn = "P × R × T / 100 with total maturity amount",
            descriptionHi = "मूलधन, दर और समय पर साधारण ब्याज",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Savings,
            keywords = listOf("interest", "simple", "principal", "bank")
        ),
        UtilityTool(
            id = "calc_compound_interest",
            nameEn = "Compound Interest Calculator",
            nameHi = "चक्रवृद्धि ब्याज कैलकुलेटर",
            descriptionEn = "Annual, semi-annual, quarterly, and monthly compounding",
            descriptionHi = "विभिन्न समयावधि पर चक्रवृद्धि ब्याज",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.TrendingUp,
            keywords = listOf("compound", "interest", "wealth", "investment")
        ),
        UtilityTool(
            id = "calc_age",
            nameEn = "Age Calculator",
            nameHi = "आयु कैलकुलेटर",
            descriptionEn = "Exact age in years, months, days, hours, and next birthday countdown",
            descriptionHi = "वर्ष, माह, दिन में सटीक आयु और अगला जन्मदिन",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Cake,
            keywords = listOf("age", "birthday", "years", "date of birth"),
            isPopular = true,
            isFeatured = true
        ),
        UtilityTool(
            id = "calc_date_diff",
            nameEn = "Date Difference Calculator",
            nameHi = "दिनांक अंतर कैलकुलेटर",
            descriptionEn = "Calculate days, weeks, and months between two calendar dates",
            descriptionHi = "दो तिथियों के बीच दिनों, हफ्तों और महीनों का अंतर",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.DateRange,
            keywords = listOf("days", "calendar", "difference", "duration")
        ),
        UtilityTool(
            id = "calc_time",
            nameEn = "Time Calculator",
            nameHi = "समय कैलकुलेटर",
            descriptionEn = "Add or subtract hours, minutes, and seconds",
            descriptionHi = "घंटे, मिनट और सेकंड जोड़ें या घटाएं",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Timer,
            keywords = listOf("time", "duration", "hours", "minutes")
        ),
        UtilityTool(
            id = "calc_average",
            nameEn = "Average Calculator",
            nameHi = "औसत कैलकुलेटर",
            descriptionEn = "Mean, median, mode, minimum, maximum, and sum of series",
            descriptionHi = "माध्य, माध्यिका, बहुलक और योग",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Analytics,
            keywords = listOf("mean", "median", "statistics", "sum")
        ),
        UtilityTool(
            id = "calc_ratio",
            nameEn = "Ratio Calculator",
            nameHi = "अनुपात कैलकुलेटर",
            descriptionEn = "Solve proportions A : B = C : D and simplify ratios",
            descriptionHi = "समानुपात हल करें और अनुपात सरल करें",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.AspectRatio,
            keywords = listOf("ratio", "proportion", "scale")
        ),
        UtilityTool(
            id = "calc_profit_loss",
            nameEn = "Profit & Loss Calculator",
            nameHi = "लाभ और हानि कैलकुलेटर",
            descriptionEn = "Cost price, selling price, profit/loss percentage",
            descriptionHi = "क्रय मूल्य, विक्रय मूल्य, लाभ/हानि प्रतिशत",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.MonetizationOn,
            keywords = listOf("business", "profit", "loss", "margin")
        ),
        UtilityTool(
            id = "calc_salary",
            nameEn = "Salary Calculator",
            nameHi = "वेतन कैलकुलेटर",
            descriptionEn = "Annual CTC to monthly in-hand, deductions, and daily rate",
            descriptionHi = "वार्षिक सीटीसी से मासिक वेतन और कटौतियां",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Payments,
            keywords = listOf("salary", "ctc", "monthly", "inhand", "job")
        ),
        UtilityTool(
            id = "calc_tip",
            nameEn = "Tip Calculator",
            nameHi = "टिप कैलकुलेटर",
            descriptionEn = "Calculate tip percentage and total bill amount",
            descriptionHi = "टिप प्रतिशत और कुल बिल की गणना",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.RoomService,
            keywords = listOf("tip", "restaurant", "dining", "bill")
        ),
        UtilityTool(
            id = "calc_split_bill",
            nameEn = "Split Bill Calculator",
            nameHi = "बिल विभाजन कैलकुलेटर",
            descriptionEn = "Evenly divide restaurant and group bills among people",
            descriptionHi = "मित्रों में रेस्टोरेंट बिल समान रूप से विभाजित करें",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Group,
            keywords = listOf("split", "friends", "group", "share")
        ),
        UtilityTool(
            id = "calc_bmi",
            nameEn = "BMI Calculator",
            nameHi = "बीएमआई कैलकुलेटर",
            descriptionEn = "Body Mass Index, category classification, and healthy weight range",
            descriptionHi = "बॉडी मास इंडेक्स और स्वस्थ वजन सीमा",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.FitnessCenter,
            keywords = listOf("bmi", "weight", "height", "health", "fitness"),
            isPopular = true
        ),
        UtilityTool(
            id = "calc_bmr",
            nameEn = "BMR Calculator",
            nameHi = "बीएमआर कैलकुलेटर",
            descriptionEn = "Basal Metabolic Rate & daily caloric needs based on activity",
            descriptionHi = "दैनिक कैलोरी आवश्यकता और बीएमआर",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.LocalFireDepartment,
            keywords = listOf("bmr", "calories", "metabolism", "diet")
        ),
        UtilityTool(
            id = "calc_fuel_cost",
            nameEn = "Fuel Cost Calculator",
            nameHi = "ईंधन लागत कैलकुलेटर",
            descriptionEn = "Distance, vehicle mileage, fuel price, and total trip expense",
            descriptionHi = "दूरी, माइलेज और ईंधन मूल्य से यात्रा व्यय",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.LocalGasStation,
            keywords = listOf("petrol", "diesel", "fuel", "mileage", "trip")
        ),
        UtilityTool(
            id = "calc_electricity",
            nameEn = "Electricity Bill Estimator",
            nameHi = "बिजली बिल अनुमानक",
            descriptionEn = "Appliance wattage, usage hours, rate per kWh, and monthly cost",
            descriptionHi = "उपकरण वाट, घंटे और यूनिट दर से मासिक बिजली बिल",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Bolt,
            keywords = listOf("power", "electricity", "watts", "kwh", "bill")
        ),
        UtilityTool(
            id = "calc_unit_price",
            nameEn = "Unit Price Calculator",
            nameHi = "इकाई मूल्य कैलकुलेटर",
            descriptionEn = "Compare package pricing to find the best deal per gram/liter/unit",
            descriptionHi = "प्रति ग्राम/लीटर सबसे सस्ता सौदा पता करें",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Compare,
            keywords = listOf("shopping", "grocery", "unit price", "deal")
        ),
        UtilityTool(
            id = "calc_speed_distance_time",
            nameEn = "Speed, Distance & Time",
            nameHi = "गति, दूरी और समय",
            descriptionEn = "Solve for any variable: Speed = Distance / Time",
            descriptionHi = "गति, दूरी अथवा समय में से किसी एक की गणना करें",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.Speed,
            keywords = listOf("speed", "distance", "time", "physics", "travel")
        ),
        UtilityTool(
            id = "calc_sqrt",
            nameEn = "Square & Cube Root",
            nameHi = "वर्गमूल और घनमूल",
            descriptionEn = "Calculate square root, cube root, square, and cube instantly",
            descriptionHi = "वर्गमूल, घनमूल, वर्ग और घन की त्वरित गणना",
            category = ToolCategory.CALCULATORS,
            icon = Icons.Default.SquareFoot,
            keywords = listOf("sqrt", "cube", "root", "square", "math")
        ),

        // Unit Converters
        UtilityTool(
            id = "conv_length",
            nameEn = "Length Converter",
            nameHi = "लंबाई परिवर्तक",
            descriptionEn = "Meters, kilometers, centimeters, feet, inches, miles, yards",
            descriptionHi = "मीटर, किमी, सेंटीमीटर, फीट, इंच, मील",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.Straighten,
            keywords = listOf("meter", "feet", "inch", "km", "miles"),
            isPopular = true
        ),
        UtilityTool(
            id = "conv_weight",
            nameEn = "Weight & Mass Converter",
            nameHi = "वजन परिवर्तक",
            descriptionEn = "Kilograms, grams, pounds, ounces, tons, stones",
            descriptionHi = "किलोग्राम, ग्राम, पाउंड, औंस, टन",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.Scale,
            keywords = listOf("kg", "grams", "lbs", "pounds", "weight"),
            isPopular = true
        ),
        UtilityTool(
            id = "conv_temperature",
            nameEn = "Temperature Converter",
            nameHi = "तापमान परिवर्तक",
            descriptionEn = "Celsius, Fahrenheit, and Kelvin temperature scales",
            descriptionHi = "सेल्सियस, फारेनहाइट और केल्विन",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.Thermostat,
            keywords = listOf("celsius", "fahrenheit", "kelvin", "heat")
        ),
        UtilityTool(
            id = "conv_area",
            nameEn = "Area Converter",
            nameHi = "क्षेत्रफल परिवर्तक",
            descriptionEn = "Square meters, sq feet, acres, hectares, sq km, sq miles",
            descriptionHi = "वर्ग मीटर, वर्ग फीट, एकड़, हेक्टेयर",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.CropLandscape,
            keywords = listOf("acre", "hectare", "sqft", "area", "land")
        ),
        UtilityTool(
            id = "conv_volume",
            nameEn = "Volume Converter",
            nameHi = "आयतन परिवर्तक",
            descriptionEn = "Liters, milliliters, gallons, cubic meters, cups, fluid ounces",
            descriptionHi = "लीटर, मिलीलीटर, गैलन, कप",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.Opacity,
            keywords = listOf("liter", "ml", "gallon", "cup", "fluid")
        ),
        UtilityTool(
            id = "conv_speed",
            nameEn = "Speed Converter",
            nameHi = "गति परिवर्तक",
            descriptionEn = "km/h, mph, m/s, knots, ft/s",
            descriptionHi = "किमी/घंटा, मील/घंटा, मीटर/सेकंड, समुद्री मील",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.FastForward,
            keywords = listOf("kmh", "mph", "knots", "velocity")
        ),
        UtilityTool(
            id = "conv_time",
            nameEn = "Time Unit Converter",
            nameHi = "समय इकाई परिवर्तक",
            descriptionEn = "Seconds, minutes, hours, days, weeks, months, years",
            descriptionHi = "सेकंड, मिनट, घंटे, दिन, सप्ताह, वर्ष",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.HourglassEmpty,
            keywords = listOf("seconds", "minutes", "hours", "days")
        ),
        UtilityTool(
            id = "conv_digital",
            nameEn = "Digital Storage Converter",
            nameHi = "डिजिटल मेमोरी परिवर्तक",
            descriptionEn = "Bytes, KB, MB, GB, TB, PB (Binary & Decimal)",
            descriptionHi = "बाइट्स, केबी, एमबी, जीबी, टीबी",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.Memory,
            keywords = listOf("bytes", "kb", "mb", "gb", "tb")
        ),
        UtilityTool(
            id = "conv_num_base",
            nameEn = "Number Base Converter",
            nameHi = "संख्या आधार परिवर्तक",
            descriptionEn = "Binary (base 2), Decimal (10), Hexadecimal (16), Octal (8)",
            descriptionHi = "बाइनरी, दशमलव, हेक्साडेसिमल, अष्टाधारी",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.Numbers,
            keywords = listOf("binary", "hex", "octal", "decimal", "base")
        ),
        UtilityTool(
            id = "conv_currency",
            nameEn = "Currency Converter",
            nameHi = "मुद्रा परिवर्तक",
            descriptionEn = "Live & offline manual exchange rate conversion for 30+ currencies",
            descriptionHi = "30+ मुद्राओं के लिए विनिमय दर रूपांतरण",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.AttachMoney,
            keywords = listOf("usd", "inr", "eur", "gbp", "forex", "money"),
            isPopular = true,
            isFeatured = true
        ),
        UtilityTool(
            id = "conv_pressure",
            nameEn = "Pressure Converter",
            nameHi = "दबाव परिवर्तक",
            descriptionEn = "Pascals, bar, psi, atmosphere (atm), mmHg",
            descriptionHi = "पास्कल, बार, पीएसआई, वायुमंडलीय दबाव",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.Compress,
            keywords = listOf("psi", "bar", "pascal", "atm", "tire")
        ),
        UtilityTool(
            id = "conv_power",
            nameEn = "Power Converter",
            nameHi = "शक्ति परिवर्तक",
            descriptionEn = "Watts, Kilowatts, Horsepower (hp), BTU/hr",
            descriptionHi = "वाट, किलोवाट, हॉर्सपावर",
            category = ToolCategory.CONVERTERS,
            icon = Icons.Default.ElectricMeter,
            keywords = listOf("watts", "hp", "horsepower", "kw")
        ),

        // Text Utilities
        UtilityTool(
            id = "text_counter",
            nameEn = "Word & Character Counter",
            nameHi = "शब्द और वर्ण गणक",
            descriptionEn = "Count words, characters, sentences, paragraphs, and reading time",
            descriptionHi = "शब्द, वर्ण, वाक्य, पैराग्राफ और पढ़ने का समय",
            category = ToolCategory.TEXT_TOOLS,
            icon = Icons.Default.Article,
            keywords = listOf("words", "characters", "length", "reading time"),
            isPopular = true
        ),
        UtilityTool(
            id = "text_case",
            nameEn = "Case Converter",
            nameHi = "केस परिवर्तक",
            descriptionEn = "UPPERCASE, lowercase, Title Case, camelCase, snake_case",
            descriptionHi = "अपरकेस, लोअरकेस, टाइटल केस, कैमलकेस",
            category = ToolCategory.TEXT_TOOLS,
            icon = Icons.Default.Abc,
            keywords = listOf("uppercase", "lowercase", "capitalize", "format")
        ),
        UtilityTool(
            id = "text_cleaner",
            nameEn = "Text Cleaner & Formatter",
            nameHi = "टेक्स्ट शोधक",
            descriptionEn = "Remove extra spaces, duplicate lines, and sort alphabetically",
            descriptionHi = "अतिरिक्त स्पेस हटाएं, डुप्लिकेट लाइनें हटाएं, क्रमबद्ध करें",
            category = ToolCategory.TEXT_TOOLS,
            icon = Icons.Default.CleaningServices,
            keywords = listOf("clean", "spaces", "trim", "duplicates", "sort")
        ),
        UtilityTool(
            id = "text_replace",
            nameEn = "Find & Replace",
            nameHi = "खोजें और बदलें",
            descriptionEn = "Search and replace text with case sensitivity and whole word match",
            descriptionHi = "टेक्स्ट खोजें और आसानी से बदलें",
            category = ToolCategory.TEXT_TOOLS,
            icon = Icons.Default.FindReplace,
            keywords = listOf("find", "replace", "search", "substitute")
        ),
        UtilityTool(
            id = "text_slug",
            nameEn = "Text to Slug",
            nameHi = "टेक्स्ट से यूआरएल स्लग",
            descriptionEn = "Convert titles and phrases to SEO-friendly URL slugs",
            descriptionHi = "शीर्षक को यूआरएल स्लग में बदलें",
            category = ToolCategory.TEXT_TOOLS,
            icon = Icons.Default.Link,
            keywords = listOf("slug", "url", "seo", "hyphen")
        ),
        UtilityTool(
            id = "text_encode",
            nameEn = "URL & Base64 Encoder",
            nameHi = "यूआरएल और बेस64 एन्कोडर",
            descriptionEn = "Encode and decode standard URL components and Base64 strings",
            descriptionHi = "यूआरएल और बेस64 एन्कोड/डिकोड करें",
            category = ToolCategory.TEXT_TOOLS,
            icon = Icons.Default.LockReset,
            keywords = listOf("base64", "url", "encode", "decode")
        ),
        UtilityTool(
            id = "text_lorem",
            nameEn = "Lorem Ipsum Generator",
            nameHi = "लोरेम इप्सम जनरेटर",
            descriptionEn = "Generate placeholder dummy paragraphs, sentences, and words",
            descriptionHi = "डमी डमी पैराग्राफ और वाक्य उत्पन्न करें",
            category = ToolCategory.TEXT_TOOLS,
            icon = Icons.Default.Subject,
            keywords = listOf("lorem", "ipsum", "dummy", "placeholder")
        ),
        UtilityTool(
            id = "text_password",
            nameEn = "Secure Password Generator",
            nameHi = "सुरक्षित पासवर्ड जनरेटर",
            descriptionEn = "Configurable length, symbols, numbers, and entropy strength score",
            descriptionHi = "कस्टमाइज़ेबल सुरक्षित पासवर्ड और शक्ति संकेतक",
            category = ToolCategory.TEXT_TOOLS,
            icon = Icons.Default.Password,
            keywords = listOf("password", "security", "random", "generator"),
            isPopular = true,
            isFeatured = true
        ),

        // QR Code & Barcode
        UtilityTool(
            id = "qr_generator",
            nameEn = "QR Code Generator",
            nameHi = "क्यूआर कोड जनरेटर",
            descriptionEn = "Create custom QR codes for Website URL, Wi-Fi, Contact, Text, and Phone",
            descriptionHi = "वेबसाइट, वाई-फाई, संपर्क, टेक्स्ट के लिए क्यूआर कोड बनाएं",
            category = ToolCategory.QR_BARCODE,
            icon = Icons.Default.QrCode2,
            keywords = listOf("qr", "barcode", "wifi", "contact", "generator"),
            isPopular = true,
            isFeatured = true
        ),
        UtilityTool(
            id = "qr_scanner",
            nameEn = "QR & Barcode Scanner",
            nameHi = "क्यूआर व बारकोड स्कैनर",
            descriptionEn = "Fast CameraX scanner for all 1D/2D QR codes and retail barcodes",
            descriptionHi = "क्यूआर और बारकोड के लिए त्वरित कैमरा स्कैनर",
            category = ToolCategory.QR_BARCODE,
            icon = Icons.Default.QrCodeScanner,
            keywords = listOf("scan", "camera", "barcode", "reader"),
            isPopular = true
        ),

        // Image Tools
        UtilityTool(
            id = "img_compress",
            nameEn = "Image Compressor",
            nameHi = "छवि कंप्रेसर",
            descriptionEn = "Reduce image file size locally with quality control and preview",
            descriptionHi = "गुणवत्ता नियंत्रण के साथ छवि का आकार छोटा करें",
            category = ToolCategory.IMAGE_TOOLS,
            icon = Icons.Default.Compress,
            keywords = listOf("compress", "photo", "reduce", "mb", "kb"),
            isPopular = true
        ),
        UtilityTool(
            id = "img_resize",
            nameEn = "Image Resizer",
            nameHi = "छवि रिसाइज़र",
            descriptionEn = "Scale width and height dimensions with aspect ratio lock",
            descriptionHi = "छवि की चौड़ाई और ऊंचाई को अनुकूलित करें",
            category = ToolCategory.IMAGE_TOOLS,
            icon = Icons.Default.PhotoSizeSelectLarge,
            keywords = listOf("resize", "dimensions", "width", "height")
        ),
        UtilityTool(
            id = "img_convert",
            nameEn = "Format Converter (PNG/JPG/WEBP)",
            nameHi = "छवि प्रारूप परिवर्तक",
            descriptionEn = "Convert between PNG, JPG, and WEBP formats offline",
            descriptionHi = "पीएनजी, जेपीजी और वेबपी प्रारूपों में रूपांतरित करें",
            category = ToolCategory.IMAGE_TOOLS,
            icon = Icons.Default.Transform,
            keywords = listOf("png", "jpg", "jpeg", "webp", "format")
        ),
        UtilityTool(
            id = "img_base64",
            nameEn = "Image to Base64",
            nameHi = "छवि से बेस64",
            descriptionEn = "Convert photo to Data URI Base64 string with copy action",
            descriptionHi = "छवि को बेस64 स्ट्रिंग में बदलें",
            category = ToolCategory.IMAGE_TOOLS,
            icon = Icons.Default.DataArray,
            keywords = listOf("base64", "datauri", "image string")
        ),
        UtilityTool(
            id = "img_color_picker",
            nameEn = "Image Color Palette Picker",
            nameHi = "छवि रंग पिकर",
            descriptionEn = "Extract dominant colors and hex codes from any selected photo",
            descriptionHi = "किसी भी फोटो से प्रमुख रंग और हेक्स कोड निकालें",
            category = ToolCategory.IMAGE_TOOLS,
            icon = Icons.Default.Palette,
            keywords = listOf("color", "hex", "palette", "picker")
        ),

        // Developer Tools
        UtilityTool(
            id = "dev_json",
            nameEn = "JSON Formatter & Validator",
            nameHi = "जेएसओएन फॉर्मेटर",
            descriptionEn = "Prettify, minify, and validate JSON syntax with error pointers",
            descriptionHi = "जेएसओएन को सुंदर बनाएं और त्रुटियां जांचें",
            category = ToolCategory.DEV_TOOLS,
            icon = Icons.Default.DataObject,
            keywords = listOf("json", "format", "validate", "prettify", "developer"),
            isPopular = true
        ),
        UtilityTool(
            id = "dev_color_conv",
            nameEn = "HEX to RGB & RGB to HEX",
            nameHi = "हेक्स से आरजीबी परिवर्तक",
            descriptionEn = "Bi-directional color code converter with live preview box",
            descriptionHi = "रंग कोड परिवर्तक और पूर्वावलोकन",
            category = ToolCategory.DEV_TOOLS,
            icon = Icons.Default.ColorLens,
            keywords = listOf("hex", "rgb", "color", "code", "css")
        ),
        UtilityTool(
            id = "dev_uuid",
            nameEn = "UUID / GUID Generator",
            nameHi = "यूयूआईडी जनरेटर",
            descriptionEn = "Generate cryptographically secure v4 UUIDs in bulk",
            descriptionHi = "सुरक्षित यूयूआईडी उत्पन्न करें",
            category = ToolCategory.DEV_TOOLS,
            icon = Icons.Default.Fingerprint,
            keywords = listOf("uuid", "guid", "id", "random")
        ),
        UtilityTool(
            id = "dev_timestamp",
            nameEn = "Epoch & Unix Timestamp",
            nameHi = "यूनिक्स टाइमस्टैम्प",
            descriptionEn = "Convert Unix seconds/milliseconds to human date and vice versa",
            descriptionHi = "यूनिक्स टाइमस्टैम्प से दिनांक में रूपांतरण",
            category = ToolCategory.DEV_TOOLS,
            icon = Icons.Default.AccessTime,
            keywords = listOf("timestamp", "epoch", "unix", "date")
        ),
        UtilityTool(
            id = "dev_hash",
            nameEn = "Hash Generator",
            nameHi = "हैश जनरेटर",
            descriptionEn = "MD5, SHA-1, SHA-256, and SHA-512 cryptographic hashes",
            descriptionHi = "एमडी5, एसएचए-1, एसएचए-256 हैश उत्पन्न करें",
            category = ToolCategory.DEV_TOOLS,
            icon = Icons.Default.Tag,
            keywords = listOf("sha256", "md5", "hash", "crypto")
        ),

        // Productivity Tools
        UtilityTool(
            id = "prod_todo",
            nameEn = "To-Do List & Tasks",
            nameHi = "कार्य सूची (टू-डू)",
            descriptionEn = "Manage tasks, priority levels, categories, and completion states",
            descriptionHi = "दैनिक कार्यों और प्राथमिकताओं को प्रबंधित करें",
            category = ToolCategory.PRODUCTIVITY,
            icon = Icons.Default.Checklist,
            keywords = listOf("todo", "task", "list", "checklist"),
            isPopular = true,
            isFeatured = true
        ),
        UtilityTool(
            id = "prod_notes",
            nameEn = "Notes Manager",
            nameHi = "नोट्स प्रबंधक",
            descriptionEn = "Rich notes organizer with colors, quick edit, and search",
            descriptionHi = "सुंदर रंगों और त्वरित संपादन के साथ नोट्स",
            category = ToolCategory.PRODUCTIVITY,
            icon = Icons.Default.EditNote,
            keywords = listOf("note", "memo", "write", "diary"),
            isPopular = true
        ),
        UtilityTool(
            id = "prod_pomodoro",
            nameEn = "Pomodoro Focus Timer",
            nameHi = "पोमोडोरो फोकस टाइमर",
            descriptionEn = "25m focus, 5m short break, and 15m long break intervals",
            descriptionHi = "25 मिनट फोकस और ब्रेक अंतराल के साथ अध्ययन टाइमर",
            category = ToolCategory.PRODUCTIVITY,
            icon = Icons.Default.PlayCircle,
            keywords = listOf("pomodoro", "study", "focus", "work", "timer"),
            isPopular = true,
            isFeatured = true
        ),
        UtilityTool(
            id = "prod_stopwatch",
            nameEn = "Stopwatch with Laps",
            nameHi = "लैप सहित स्टॉपवॉच",
            descriptionEn = "Precision milliseconds stopwatch with lap splits recording",
            descriptionHi = "सटीक मिलीसेकंड स्टॉपवॉच और लैप्स",
            category = ToolCategory.PRODUCTIVITY,
            icon = Icons.Default.Timer,
            keywords = listOf("stopwatch", "lap", "race", "chronometer")
        ),
        UtilityTool(
            id = "prod_expenses",
            nameEn = "Expense Tracker",
            nameHi = "व्यय ट्रैकर",
            descriptionEn = "Log daily expenses, category breakdowns, and total spend summary",
            descriptionHi = "दैनिक खर्चों और बजट का हिसाब रखें",
            category = ToolCategory.PRODUCTIVITY,
            icon = Icons.Default.AccountBalanceWallet,
            keywords = listOf("expense", "money", "budget", "tracker"),
            isPopular = true
        ),

        // Daily Life Tools
        UtilityTool(
            id = "life_world_clock",
            nameEn = "World Clock",
            nameHi = "विश्व घड़ी",
            descriptionEn = "Live timezones for major global cities with day/night indicator",
            descriptionHi = "प्रमुख वैश्विक शहरों का सजीव समय",
            category = ToolCategory.DAILY_LIFE,
            icon = Icons.Default.Public,
            keywords = listOf("clock", "timezone", "world", "gmt", "ist")
        ),
        UtilityTool(
            id = "life_water",
            nameEn = "Water Intake Estimator",
            nameHi = "दैनिक जल सेवन अनुमानक",
            descriptionEn = "Recommended daily water consumption based on weight & activity",
            descriptionHi = "वजन और गतिविधि के अनुसार आवश्यक पानी की मात्रा",
            category = ToolCategory.DAILY_LIFE,
            icon = Icons.Default.WaterDrop,
            keywords = listOf("water", "health", "hydration", "drink")
        ),
        UtilityTool(
            id = "life_cooking",
            nameEn = "Cooking Measurement Converter",
            nameHi = "रसोई माप परिवर्तक",
            descriptionEn = "Tablespoons, teaspoons, cups, grams, ounces, and milliliters",
            descriptionHi = "चम्मच, कप, ग्राम और मिलीलीटर रूपांतरण",
            category = ToolCategory.DAILY_LIFE,
            icon = Icons.Default.Restaurant,
            keywords = listOf("recipe", "cooking", "tablespoon", "cup", "kitchen")
        ),
        UtilityTool(
            id = "life_shoe_size",
            nameEn = "Shoe Size Reference",
            nameHi = "जूता आकार संदर्भ",
            descriptionEn = "US, UK, EU, and CM shoe size conversion chart",
            descriptionHi = "यूएस, यूके, ईयू जूता आकार संदर्भ",
            category = ToolCategory.DAILY_LIFE,
            icon = Icons.Default.ShoppingBag,
            keywords = listOf("shoe", "size", "footwear", "chart")
        ),

        // Fun and Random Tools
        UtilityTool(
            id = "fun_coin_flip",
            nameEn = "Coin Flip 3D",
            nameHi = "सिक्का उछालें",
            descriptionEn = "Realistic Heads or Tails with smooth physics rotation",
            descriptionHi = "हेड्स या टेल्स का निष्पक्ष निर्णय",
            category = ToolCategory.FUN_RANDOM,
            icon = Icons.Default.Paid,
            keywords = listOf("coin", "flip", "toss", "heads", "tails"),
            isPopular = true
        ),
        UtilityTool(
            id = "fun_dice",
            nameEn = "Dice Roller",
            nameHi = "पासा रोलर (डाइस)",
            descriptionEn = "Roll 1 to 4 dice with animated values and sum display",
            descriptionHi = "1 से 4 पासे फेंकें और योग देखें",
            category = ToolCategory.FUN_RANDOM,
            icon = Icons.Default.Casino,
            keywords = listOf("dice", "roll", "random", "boardgame"),
            isPopular = true
        ),
        UtilityTool(
            id = "fun_spin_wheel",
            nameEn = "Interactive Decision Wheel",
            nameHi = "निर्णय चक्र (स्पिन व्हील)",
            descriptionEn = "Spin customizable colorful wheel to make choices effortlessly",
            descriptionHi = "विकल्पों के साथ रंगीन पहिया घुमाएं",
            category = ToolCategory.FUN_RANDOM,
            icon = Icons.Default.PieChart,
            keywords = listOf("wheel", "spin", "roulette", "decision", "picker"),
            isPopular = true,
            isFeatured = true
        ),
        UtilityTool(
            id = "fun_random_num",
            nameEn = "Random Number Picker",
            nameHi = "यादृच्छिक संख्या पिकर",
            descriptionEn = "Generate random integers within custom min/max range",
            descriptionHi = "कस्टम न्यूनतम और अधिकतम सीमा में यादृच्छिक संख्या",
            category = ToolCategory.FUN_RANDOM,
            icon = Icons.Default.Shuffle,
            keywords = listOf("random", "number", "generator", "lottery")
        ),
        UtilityTool(
            id = "fun_team_gen",
            nameEn = "Random Team Generator",
            nameHi = "यादृच्छिक टीम जनरेटर",
            descriptionEn = "Shuffle participant names into balanced groups and teams",
            descriptionHi = "नामों को संतुलित टीमों में विभाजित करें",
            category = ToolCategory.FUN_RANDOM,
            icon = Icons.Default.Groups,
            keywords = listOf("team", "group", "shuffle", "split", "game")
        ),
        UtilityTool(
            id = "fun_decision",
            nameEn = "Yes or No Decision Maker",
            nameHi = "हाँ या ना निर्णय कर्ता",
            descriptionEn = "Instant animated answer generator for quick dilemmas",
            descriptionHi = "त्वरित असमंजस के लिए हाँ या ना निर्णय",
            category = ToolCategory.FUN_RANDOM,
            icon = Icons.Default.HelpOutline,
            keywords = listOf("yes", "no", "decision", "oracle")
        ),
        UtilityTool(
            id = "fun_random_color",
            nameEn = "Random Color Generator",
            nameHi = "यादृच्छिक रंग जनरेटर",
            descriptionEn = "Discover fresh vibrant hex colors with copyable codes",
            descriptionHi = "नए रंग खोजें और हेक्स कोड कॉपी करें",
            category = ToolCategory.FUN_RANDOM,
            icon = Icons.Default.ColorLens,
            keywords = listOf("color", "hex", "rgb", "generator")
        )
    )

    fun getToolById(id: String): UtilityTool? = tools.find { it.id == id }

    fun searchTools(query: String): List<UtilityTool> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        return tools.filter { tool ->
            tool.nameEn.lowercase().contains(q) ||
            tool.nameHi.contains(q) ||
            tool.descriptionEn.lowercase().contains(q) ||
            tool.category.titleEn.lowercase().contains(q) ||
            tool.keywords.any { it.lowercase().contains(q) }
        }
    }
}

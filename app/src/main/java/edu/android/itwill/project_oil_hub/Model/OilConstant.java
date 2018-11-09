package edu.android.itwill.project_oil_hub.Model;

import edu.android.itwill.project_oil_hub.R;

public interface OilConstant {

    String OPINET_API_KEY = "&code=F375181018";
    //String DAUM_MAP_API_KEY = "9b3ace49af03db850b0039a6e940abc3";

    String[] POLL_DIV_CD =
        {"SKE", "GSC", "HDO", "SOL", "RTO", "RTX", "NHO", "ETC", "E1G", "SKG"};
    String[] BRANDS =
        {
            "SK에너지", "GS칼텍스", "현대오일뱅크",
            "S-OIL", "자영알뜰", "고속도로알뜰",
            "농협알뜰", "자가상표", "E1", "SK가스"
        };
    int[] BRANDIMAGES =
        {
            R.drawable.sk_logo, R.drawable.gs_logo, R.drawable.hyundai_logo,
            R.drawable.s_oil_logo, R.drawable.logo, R.drawable.logo,
            R.drawable.nh_logo, R.drawable.pb_logo, R.drawable.e1_logo, R.drawable.sk_logo
        };

    int[] RADIUSES = {1000, 2000, 3000, 4000, 5000};

    String[] PRODUCT_CODES =
        {"B027", "D047", "B034", "K015", "C004"};
    String[] PRODUCTS =
        {"휘발유", "경유", "고급휘발유", "LPG", "등유"};

    String[] SIDO_CODE =
        {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "14", "15", "16", "17", "18", "19"
        };
    String[] SIDO =
        {
            "서울", "경기", "강원", "충북", "충남",
            "전북", "전남", "경북", "경남", "부산",
            "제주", "대구", "인천", "광주", "대전", "울산", "세종"
        };

    String[] SEOUL_CODE =
        {
            "0101", "0102", "0103", "0104", "0105", "0106", "0107", "0108", "0109", "0110",
            "0111", "0112", "0113", "0114", "0115", "0116", "0117", "0118", "0119", "0120",
            "0121", "0122", "0123", "0124", "0125"
        };
    String[] SEOUL =
        {
            "종로구", "중구", "동대문구", "성동구", "성북구", "도봉구", "서대문구", "은평구", "마포구", "용산구",
            "영등포구", "동작구", "강남구", "강동구", "강서구", "구로구", "관악구", "노원구", "양천구", "중랑구",
            "서초구", "송파구", "광진구", "강북구", "금천구"
        };

    String[] GYEONGGI_CODE =
        {
            "0201", "0202", "0203", "0204", "0205", "0206", "0207", "0208", "0209", "0210",
            "0211", "0212", "0213", "0214", "0215", "0216", "0217", "0218", "0219", "0220",
            "0221", "0222", "0223", "0224", "0226", "0227", "0228", "0229", "0230", "0231",
            "0232", "0235", "0236"
        };
    String[] GYEONGGI =
        {
            "수원시", "성남시", "의정부시", "안양시", "부천시", "동두천시", "광명시", "이천시", "평택시", "구리시",
            "과천시", "안산시", "오산시", "의왕시", "군포시", "시흥시", "남양주시", "하남시", "고양시", "용인시",
            "양주시", "여주시", "화성시", "파주시", "광주시", "연천군", "포천시", "가평군",
            "양평군", "안성시", "김포시"
        };

    String[] GANGWON_CODE =
        {
            "0301", "0302", "0303", "0304", "0305", "0306", "0307", "0322", "0323", "0325",
            "0326", "0327", "0328", "0329", "0330", "0331", "0332", "0333",
        };
    String[] GANGWON =
        {
            "춘천시", "원주시", "강릉시", "속초시", "동해시", "태백시", "삼척시", "홍천군", "횡성군", "영월군",
            "평창군", "정선군", "철원군", "화천군", "양구군", "인제군", "고성군", "양양군"
        };

    String[] CHUNGBUK_CODE =
        {
            "0401", "0402", "0403", "0422", "0423", "0424", "0425", "0426", "0427", "0430", "0431"
        };
    String[] CHUNGBUK =
        {
            "청주시", "충주시", "제천시", "보은군", "옥천군", "영동군", "진천군", "괴산군", "음성군", "단양군", "증평군"
        };

    String[] CHUNGNAM_CODE =
        {
            "0502", "0503", "0504", "0505", "0506", "0507", "0508", "0521", "0526", "0527",
            "0529", "0530", "0531", "0533", "0537"
        };
    String[] CHUNGNAM =
        {
            "천안시", "공주시", "아산시", "보령시", "서산시", "논산시", "계룡시", "금산군", "부여군", "서천군",
            "청양군", "홍성군", "예산군", "당진시", "태안군"
        };

    String[] JEONBUK_CODE =
        {
            "0601", "0602", "0603", "0604", "0605", "0606", "0621", "0622", "0623", "0624",
            "0625", "0627", "0629", "0630"
        };
    String[] JEONBUK =
        {
            "전주시", "군산시", "익산시", "남원시", "정읍시", "김제시", "완주군", "진안군", "무주군", "장수군",
            "임실군", "순청군", "고창군", "부안군"
        };

    String[] JEONNAM_CODE =
        {
            "0702", "0703", "0704", "0705", "0707", "0722", "0723", "0724", "0728", "0729", "0730",
            "0731", "0732", "0733", "0734", "0735", "0737", "0738", "0739", "0740", "0741", "0742"
        };
    String[] JEONNAM =
        {
            "목포시", "여수시", "순천시", "나주시", "광양시", "담양군", "곡성군", "구례군", "고흥군", "보성군", "화순군",
            "장흥군", "강진군", "해남군", "영암군", "무안군", "함평군", "영광군", "장성군", "완도군", "진도군", "신안군"
        };

    String[] GYEONGBUK_CODE =
        {
            "0801", "0802", "0803", "0804", "0805", "0806", "0807", "0808", "0809", "0810",
            "0822", "0823", "0825", "0826", "0827", "0832", "0833", "0834", "0835", "0840",
            "0842", "0843", "0844"
        };
    String[] GYEONGBUK =
        {
            "포항시", "경주시", "김천시", "영주시", "영천시", "안동시", "구미시", "문경시", "상주시", "경산시",
            "군위군", "의성군", "청송군", "영양군", "영덕군", "청도군", "고령군", "성주군", "칠곡군", "예천군",
            "봉화군", "울진군", "울릉군"
        };

    String[] GYEONGNAM_CODE =
        {
            "0902", "0904", "0906", "0907", "0908", "0909", "0910", "0911", "0922", "0923",
            "0924", "0932", "0934", "0935", "0936", "0937", "0938", "0939"
        };
    String[] GYEONGNAM =
        {
            "창원시", "진주시", "통영시", "사천시", "김해시", "밀양시", "거제시", "양산시", "의령군", "함안군",
            "창녕군", "고성군", "남해군", "하동군", "산청군", "함양군", "거창군", "합천군"
        };

    String[] BUSAN_CODE =
        {
            "1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009", "1010",
            "1011", "1012", "1013", "1014", "1015", "1031"
        };
    String[] BUSAN =
        {
            "중구", "서구", "동구", "영도구", "부산진구", "동래구", "남구", "북구", "해운대구", "사하구",
            "강서구", "금정구", "연제구", "수영구", "사상구", "기장군"
        };

    String[] JEJU_CODE = {"1101", "1102"};
    String[] JEJU = {"제주시", "서귀포시"};

    String[] DAEGU_CODE = {"1401", "1402", "1403", "1404", "1405", "1406", "1407", "1431"};
    String[] DAEGU = {"중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"};

    String[] INCHEON_CODE = {"1501", "1502", "1503", "1504", "1505", "1506", "1507", "1508", "1531", "1532"};
    String[] INCHEON = {"중구", "동구", "미추홀구", "부평구", "서구", "남동구", "연수구", "계양구", "강화군", "옹진군"};

    String[] GWANGJU_CODE = {"1601", "1602", "1603", "1604", "1605"};
    String[] GWANGJU = {"동구", "서구", "북구", "광산구", "남구"};

    String[] DAEJEON_CODE = {"1701", "1702", "1703", "1704", "1705"};
    String[] DAEJEON = {"동구", "중구", "서구", "유성구", "대덕구"};

    String[] ULSAN_CODE = {"1801", "1802", "1803", "1804", "1831"};
    String[] ULSAN = {"중구", "남구", "동구", "북구", "울주군"};

    String[] SEJONG_CODE = {"1901"};
    String[] SEJONG = {"세종시"};

    String PRODCD_VALUE = "&prodcd=";
    String SIDO_VALUE = "&sido=";
    String AREA_VALUE = "&area=";
    String X_VALUE = "&x=";
    String Y_VALUE = "&y=";
    String RADIUS_VALUE = "&radius=";
    String SORT_VALUE = "&sort=";
    String ID_VALUE = "&id=";

    String VAR_RESULT = "RESULT";
    String VAR_OIL = "OIL";
    String VAR_UNI_ID = "UNI_ID";
    String VAR_PRODCD = "PRODCD";
    String VAR_PRODNM = "PRODNM";
    String VAR_PRICE = "PRICE";
    String VAR_DIFF = "DIFF";
    String VAR_POLL_DIV_CD = "POLL_DIV_CD";
    String VAR_OS_NM = "OS_NM";
    String VAR_VAN_ADR = "VAN_ADR";
    String VAR_NEW_ADR = "NEW_ADR";
    String VAR_GIS_X_COOR = "GIS_X_COOR";
    String VAR_GIS_Y_COOR = "GIS_Y_COOR";
    String VAR_TEL = "TEL";
    String VAR_LPG_YN = "LPG_YN";
    String VAR_MAINT_YN = "MAINT_YN";
    String VAR_CAR_WASH_YN = "CAR_WASH_YN";
    String VAR_CVS_YN = "CVS_YN";
    String VAR_OIL_PRICE = "OIL_PRICE";
    String VAR_TRADE_DT = "TRADE_DT";
    String VAR_TRADE_TM = "TRADE_TM";
    String VAR_WEEK = "WEEK";
    String VAR_STA_DT = "STA_DT";
    String VAR_END_DT = "END_DT";
    String VAR_AREA_CD = "AREA_CD";
    String VAR_DATE = "DATE";

    String PLUS = "▲";
    String MINUS = "▼";

    String CON_AVG_URL = "http://www.opinet.co.kr/api/avgAllPrice.do?out=json";

    String SIDO_AVG_URL = "http://www.opinet.co.kr/api/avgSidoPrice.do?out=json";

    String SIGUN_AVG_URL = "http://www.opinet.co.kr/api/avgSigunPrice.do?out=json";

    String WEEK_DAY_AVG_URL = "http://www.opinet.co.kr/api/avgRecentPrice.do?out=json";

    String LAST_WEEK_AVG_URL = "http://www.opinet.co.kr/api/avgLastWeek.do?out=json";

    String LOW_TOP10_URL = "http://www.opinet.co.kr/api/lowTop10.do?out=json";

    String AROUND_ALL_URL = "http://www.opinet.co.kr/api/aroundAll.do?out=json";

    String DETAIL_INFO_URL = "http://www.opinet.co.kr/api/detailById.do?out=json";

    String SEARCH_NAME_URL = "http://www.opinet.co.kr/api/searchByName.do?out=json";
}

package com.test.juxiaohui.mdxc.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihao on 15/5/5.
 */
public class CountryCode_Temp
{
    public String mCountry;
    public String mCode;

    public CountryCode_Temp(String country, String code)
    {
        mCountry = country;
        mCode = code;
    }


public static List<CountryCode_Temp> getDefaultCodes()
{
//    +95 Myanmar
//        +86 China
//        +65 Singapore
//        +66 Thailand
//        +81 Japan
//        +82 Korea
//        +852 Hong Kong
//    +1 US&Canada
//        +44 United Kindom
//    +61 Australia
    List<CountryCode_Temp> codeList = new ArrayList<CountryCode_Temp>();

    codeList.add(new CountryCode_Temp("Myanmar", "+95"));
    codeList.add(new CountryCode_Temp("China", "+86"));
    codeList.add(new CountryCode_Temp("Singapore", "+65"));
    codeList.add(new CountryCode_Temp("Thailand", "+66"));
    codeList.add(new CountryCode_Temp("Japan", "+81"));
    codeList.add(new CountryCode_Temp("Korea", "+82"));
    codeList.add(new CountryCode_Temp("Hong Kong", "+852"));
    codeList.add(new CountryCode_Temp("US&Canada", "+1"));
    codeList.add(new CountryCode_Temp("United Kindom", "+44"));
    codeList.add(new CountryCode_Temp("Australia", "+61"));
    return codeList;
}

public static List<String> convertCodeListToString(List<CountryCode_Temp> codeList)
{
    List<String> listStr = new ArrayList<String>();
    for (CountryCode_Temp code:codeList)
    {
        listStr.add(code.mCode + "(" + code.mCountry + ")");
    }
    return listStr;
}

};

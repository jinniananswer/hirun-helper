package com.microtomato.hirun.automatic;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.HirunHelperApplication;
import com.microtomato.hirun.modules.system.entity.po.City;
import com.microtomato.hirun.modules.system.entity.po.County;
import com.microtomato.hirun.modules.system.entity.po.Province;
import com.microtomato.hirun.modules.system.service.ICityService;
import com.microtomato.hirun.modules.system.service.ICountyService;
import com.microtomato.hirun.modules.system.service.IProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Steven
 * @date 2019-11-15
 */

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = HirunHelperApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityPickerGenerator {

    @Autowired
    private IProvinceService provinceServiceImpl;

    @Autowired
    private ICityService cityServiceImpl;

    @Autowired
    private ICountyService countyServiceImpl;


    //@Test
    public void createData() throws IOException {

        //文件目录
        Path rootLocation = Paths.get("C:\\Users\\Steven\\Desktop\\js");
        if (Files.notExists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        Path path = rootLocation.resolve("city-picker2.data.js");

        StringBuilder builder = new StringBuilder(1000);

        builder.append("    var ChineseDistricts = {\n");
        builder.append("            86: {\n");
        builder.append("                'A-G': [\n");
        builder.append("                    {code: '340000', address: '安徽省'},\n");
        builder.append("                    {code: '110000', address: '北京市'},\n");
        builder.append("                    {code: '500000', address: '重庆市'},\n");
        builder.append("                    {code: '350000', address: '福建省'},\n");
        builder.append("                    {code: '620000', address: '甘肃省'},\n");
        builder.append("                    {code: '440000', address: '广东省'},\n");
        builder.append("                    {code: '450000', address: '广西壮族自治区'},\n");
        builder.append("                    {code: '520000', address: '贵州省'}],\n");
        builder.append("                'H-K': [\n");
        builder.append("                    {code: '460000', address: '海南省'},\n");
        builder.append("                    {code: '130000', address: '河北省'},\n");
        builder.append("                    {code: '230000', address: '黑龙江省'},\n");
        builder.append("                    {code: '410000', address: '河南省'},\n");
        builder.append("                    {code: '420000', address: '湖北省'},\n");
        builder.append("                    {code: '430000', address: '湖南省'},\n");
        builder.append("                    {code: '320000', address: '江苏省'},\n");
        builder.append("                    {code: '360000', address: '江西省'},\n");
        builder.append("                    {code: '220000', address: '吉林省'}],\n");
        builder.append("                'L-S': [\n");
        builder.append("                    {code: '210000', address: '辽宁省'},\n");
        builder.append("                    {code: '150000', address: '内蒙古自治区'},\n");
        builder.append("                    {code: '640000', address: '宁夏回族自治区'},\n");
        builder.append("                    {code: '630000', address: '青海省'},\n");
        builder.append("                    {code: '370000', address: '山东省'},\n");
        builder.append("                    {code: '310000', address: '上海市'},\n");
        builder.append("                    {code: '140000', address: '山西省'},\n");
        builder.append("                    {code: '610000', address: '陕西省'},\n");
        builder.append("                    {code: '510000', address: '四川省'}],\n");
        builder.append("                'T-Z': [\n");
        builder.append("                    {code: '120000', address: '天津市'},\n");
        builder.append("                    {code: '650000', address: '新疆维吾尔自治区'},\n");
        builder.append("                    {code: '540000', address: '西藏自治区'},\n");
        builder.append("                    {code: '530000', address: '云南省'},\n");
        builder.append("                    {code: '330000', address: '浙江省'}]\n");
        builder.append("            },\n");

        List<Province> provinceList = provinceServiceImpl.list(Wrappers.<Province>lambdaQuery().orderByAsc(Province::getId));
        for (Province province : provinceList) {
            builder.append(String.format("            %s: {\n", province.getProvinceId()));
            List<City> cityList = cityServiceImpl.list(Wrappers.<City>lambdaQuery().eq(City::getProvinceId, province.getProvinceId()).orderByAsc(City::getId));
            for (City city : cityList) {
                builder.append(String.format("                %s: '%s',\n", city.getCityId(), city.getName()));
            }
            builder.append("            },\n");

            for (City city : cityList) {
                builder.append(String.format("            %s: {\n", city.getCityId()));
                List<County> countyList = countyServiceImpl.list(Wrappers.<County>lambdaQuery().eq(County::getCityId, city.getCityId()).orderByAsc(County::getId));
                for (County county : countyList) {
                    builder.append(String.format("                %s: '%s',\n", county.getCountyId(), county.getName()));
                }
                builder.append("            },\n");
            }

        }
        builder.append("        },\n");
        builder.append("        ;\n\n");
        builder.append("    if (typeof window !== 'undefined') {\n");
        builder.append("        window.ChineseDistricts = ChineseDistricts;\n");
        builder.append("    }\n");
        Files.write(path, builder.toString().getBytes());

    }

}

package com.snow.weather.serviceImpl;

import com.snow.weather.domain.City;
import com.snow.weather.domain.Life;
import com.snow.weather.domain.Temp;
import com.snow.weather.domain.Weather;
import com.snow.weather.persistence.UserDao;
import com.snow.weather.service.UserService;
import com.snow.weather.vo.WeatherBriefVO;
import com.snow.weather.vo.WeatherDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    private UserService userService =new UserServiceImpl();

    @Override
    public List<WeatherBriefVO> getWeatherBriefVO(City city) {
        List<Weather> weathers = userDao.getWeather(city);
        List<WeatherBriefVO> weatherBriefVOList=new ArrayList<WeatherBriefVO>();
        //今天
        WeatherBriefVO weatherBriefVOToday=new WeatherBriefVO(weathers.get(0));
        weatherBriefVOList.add(weatherBriefVOToday);
        //明天
        WeatherBriefVO weatherBriefVOTomorrow=new WeatherBriefVO(weathers.get(1));
        weatherBriefVOTomorrow.setPredictDay("明天");
        weatherBriefVOList.add(weatherBriefVOTomorrow);
        //后天
        WeatherBriefVO weatherBriefVOTheDay=new WeatherBriefVO(weathers.get(1));
        weatherBriefVOTheDay.setPredictDay("后天");
        weatherBriefVOList.add(weatherBriefVOTheDay);
        return weatherBriefVOList;
    }

    /**
     * 获取天气细况
     * @return 天气VO
     */
    @Override
    public WeatherDetailsVO getWeatherDetailsVO(City city) {

        Weather weather=userService.getCurrentWeather(city);
        Temp temp = userService.getCurrentTemp(city);
        return new WeatherDetailsVO(weather,temp);
    }

    /**
     * 获取一天24小时温度状况
     * @param city 城市
     * @return List<Temp>温度的结果集
     */
    @Override
    public List<Temp> getTemp(City city) {

        return userDao.getTemp(city);
    }

    /**
     * 15天天气情况
     * @param city 城市
     * @return 15天气的结果集
     */
    @Override
    public List<Weather> getTwoWeeksWeather(City city) {
        return userDao.getWeather(city);
    }

    /**
     * 获取生活指数
     * @param city 城市
     * @return 当日城市生活指数的结果集
     */
    @Override
    public List<Life> getLifeIndex(City city) {
        return userDao.getLife(city);
    }

    @Override
    public Weather getCurrentWeather(City city) {
        List<Weather> weathers = userDao.getWeather(city);
        return weathers.get(1);
    }

    @Override
    public Temp getCurrentTemp(City city) {
        //获取当前小时
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);//小时
        Temp temp = new Temp();
        List<Temp> temps = userDao.getTemp(city);
        for (Temp t:temps) {
            if(t.getHour()==hour){
                temp=t;
                break;
            }
        }
        return temp;
    }

}

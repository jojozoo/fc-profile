package com.orientalcomics.profile.biz.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.orientalcomics.profile.biz.base.PerfTimeAssignable;
import com.orientalcomics.profile.biz.dao.PerfTimeDAO;
import com.orientalcomics.profile.biz.model.PerfTime;
import com.orientalcomics.profile.constants.status.PerfTimeStatus;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.util.logging.ILogger;
import com.orientalcomics.profile.util.logging.ProfileLogger;
import com.orientalcomics.profile.util.time.DateTimeUtil;

@Service
public class PerfTimeService {

    private final ILogger log = ProfileLogger.getLogger(this.getClass());

    @Autowired
    PerfTimeDAO           perfTimeDAO;
    
    @Autowired
    KpiService 			  kpiService;

    public List<Integer> queryYears(int offset, int count) {
        return perfTimeDAO.queryYears(offset, count);
    }
    
    /**
     * 过滤掉用户没有参加perfTimes的perfTime
     * 
     * @param userId
     * @param perfTimes
     * @return
     */
    public Collection<PerfTime> filterNotJoinPerfTimeBy(final int userId, Collection<PerfTime> perfTimes) {
    	
    	if (perfTimes.isEmpty()) {
    		return null;
    	}
    	
    	Collection<PerfTime> filterPerfTimes = Collections2.filter(perfTimes, new Predicate<PerfTime>(){

			@Override
			public boolean apply(PerfTime perfTime) {
				return kpiService.isHaveBy(userId, perfTime.getId());
			}
			});
    	
    	return filterPerfTimes;
    }
    
    /***
     * <p> 得到每年对应的季度考核记录,返回的key是每年的时间，value是季度集合
     * 
     * <p> 如：{"2011",{"Q1,Q2,Q3,Q4"}},Map的结果是按时间最长排序的
     * 
     * @param years 改参数是限制得到的年的数目
     */
    public Map<Integer, Collection<PerfTime>> queryYearsContainQuarter(int years){
    	
    	List<Integer> yearList = perfTimeDAO.queryYears(0, years);
    	if(Collections0.isEmpty(yearList))
    		return null;
    	
    	Map<Integer,Collection<PerfTime>> yearOfQuarterMap = new TreeMap<Integer,Collection<PerfTime>>(new Comparator<Integer>(){

			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 == null || o2 == null)
                    return 0; 
				return o2.compareTo(o1);
			}});
    	
    	for(Integer year : yearList){
    		List<PerfTime> perfList = queryByYear(year);
    		if(!Collections0.isEmpty(perfList)){
    			Collection<PerfTime> quartorList = Collections2.transform(perfList, new Function<PerfTime, PerfTime>(){

					@Override
					public PerfTime apply(PerfTime perfTime) {
						if(perfTime != null)
							perfTime.setPerfTitle(StringUtils.substring(perfTime.getPerfTitle(),4,perfTime.getPerfTitle().length()));
						return perfTime;
					}});
    			
    			yearOfQuarterMap.put(year,quartorList);
    		}
    			
    	}
    	
    	return yearOfQuarterMap;
    	
    }

    public List<PerfTime> queryByYear(int perfYear) {
        return perfTimeDAO.queryByYear(perfYear);
    }
    
    public PerfTime queryByQuartor(String perfQuartor) {
        return perfTimeDAO.queryByQuartor(perfQuartor);
    }
    
    public PerfTime queryBy(int perfTimeId) {
        return perfTimeDAO.queryBy(perfTimeId);
    }
    
    public PerfTime queryByNOStatus(int perfTimeId) {
        return perfTimeDAO.queryByNOStatus(perfTimeId);
    }
    
    public PerfTime queryLastest() {
        return perfTimeDAO.queryLastest();
    }

    public int update(PerfTime model) {
        return perfTimeDAO.update(model);
    }

    public Integer save(PerfTime model) {
        return perfTimeDAO.save(model);
    }

    public void delete(int id) {
        perfTimeDAO.delete(id);
    }

    public List<PerfTime> query(int offset, int count) {
        return perfTimeDAO.queryAll(offset, count);
    }

    /**
     * 获取当前<strong>正在进行</strong>的绩效考核。
     * 
     * @return 如果当前绩效没有开始,则返回NULL。
     */
    public PerfTime getCurrent() {
        PerfTime perfTime = perfTimeDAO.queryLastestWithStatuses(Arrays.asList(PerfTimeStatus.STARTED.getId(), PerfTimeStatus.WILL_END.getId()));
        if (perfTime != null) {
            if (perfTime.status() == PerfTimeStatus.STARTED || perfTime.status() == PerfTimeStatus.WILL_END) {
                return perfTime;
            }
        }
        return null;
    }
    
    public PerfTime getCurrentWithoutStatus() {
        return perfTimeDAO.queryLastestWithStatuses(Arrays.asList(PerfTimeStatus.READY.getId(),PerfTimeStatus.STARTED.getId(), PerfTimeStatus.WILL_END.getId()));
    }
    /**
     * 获得最后一个<strong>正在进行</strong>或者<strong>已经结束</strong>的绩效考核
     * 
     * @return
     */
    public PerfTime getLastestValid() {
        return perfTimeDAO.queryLastestWithStatuses(Arrays.asList(PerfTimeStatus.STARTED.getId(), PerfTimeStatus.WILL_END.getId(), PerfTimeStatus.END.getId()));
    }

    /**
     * 返回title对应的季度第一天
     * 
     * @param perfTitle
     * @return
     */
    @SuppressWarnings("deprecation")
    public Date getPerfTimeTitleLastDay(String perfTitle) {

        try {
            String[] titles = perfTitle.split("Q");
            int season = Integer.parseInt(titles[titles.length - 1]);
            int year = Integer.parseInt(titles[0]);
            Date firstDay = new Date(year - 1900, (season - 1) * 3, 1);

            return firstDay;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCurrentId() {
        PerfTime perfTime = getCurrent();
        return perfTime == null ? 0 : perfTime.getId();
    }

    public boolean isCurrent(int perfTimeId) {
        return getCurrentId() == perfTimeId;
    }

    public PerfTime getPerfTime(int perfTimeId) {
        return perfTimeDAO.query(perfTimeId);
    }

    public int countAll() {
        return perfTimeDAO.countAll();
    }

    public int countAllYear() {
        return perfTimeDAO.countAllYear();
    }

    public void wrapPerfTimes(Collection<? extends PerfTimeAssignable> inputs) {
        if (CollectionUtils.isEmpty(inputs)) {
            return;
        }
        for (PerfTimeAssignable input : inputs) {
            input.perfTime(perfTimeDAO.query(input.perfTimeId()));
        }
    }

    /**
     * 查找当前时间已经超过绩效的结束时间的，关闭
     */
    public void endPerfTime() {

        try {

            List<PerfTime> perfTimes = perfTimeDAO.queryAll(0, 2);
            for (PerfTime perfTime : perfTimes) {

                if (perfTime.getEndTime() == null) {
                    continue;
                }

                Date endDate = new Date(perfTime.getEndTime().getTime());
                if (DateTimeUtil.compareAccurateToDate(new Date(), endDate) > 0) {
                    perfTime.setStatus(PerfTimeStatus.END.getId());
                    perfTimeDAO.update(perfTime);
                }

            }

        } catch (Exception e) {

            log.error("调用定时任务更新状态失败:PerfTimeService->endPerfTime", e);
            new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
    	Collection<Integer> ids = new ArrayList<Integer>();
    	ids.add(1);
    	ids.add(2);
    	ids.add(3);
    	
    	List<Integer> filterCollection = (List<Integer>) Collections2.filter(ids, new Predicate<Integer>(){

			@Override
			public boolean apply(Integer arg0) {
				
				return arg0 == 1 ? true : false;
			}});
    	
    	System.out.println(filterCollection);
    }
}

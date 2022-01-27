import request from '@/utils/request'

const api_sche = '/admin/hosp/schedule'

export default {
    getScheduleRule(hoscode, depcode, page, limit) {

        return request({
            url: `${api_sche}/getScheduleRule/${hoscode}/${depcode}/${page}/${limit}`,
            method: 'get'
        })
    },
    //查询排班详情
    getScheduleDetail(hoscode, depcode, workDate) {
        return request({
            url: `${api_sche}/getScheduleDetail/${hoscode}/${depcode}/${workDate}`,
            method: 'get'
        })
    },


}
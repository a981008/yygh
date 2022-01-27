import request from '@/utils/request'

const api_hosp = '/admin/hosp/hospital'
const api_dict = '/admin/cmn/dict'
const api_dept = '/admin/hosp/department'

export default {
    //医院列表
    getPageList(current, limit, searchObj) {
        return request({
            url: `${api_hosp}/list/${current}/${limit}`,
            method: 'get',
            params: searchObj
        })
    },
    //查询dictCode查询下级数据字典
    findByDictCode(dictCode) {
        return request({
            url: `${api_dict}/findByDictCode/${dictCode}`,
            method: 'get'
        })
    },

    //根据id查询下级数据字典
    findByParentId(dictCode) {
        return request({
            url: `${api_dict}/childData/${dictCode}`,
            method: 'get'
        })
    },
    // 更新状态
    updateStatus(id, status) {
        return request({
            url: `${api_hosp}/updateStatus/${id}/${status}`,
            method: 'get'
        })
    },
    //查看医院详情
    getHospById(id) {
        return request({
            url: `${api_hosp}/showHospDetail/${id}`,
            method: 'get'
        })
    },
    // 查看医院科室
    getDeptByHoscode(hoscode) {
        return request({
            url: `${api_dept}/getDeptAll/${hoscode}`,
            method: 'get'
        })
    }

}

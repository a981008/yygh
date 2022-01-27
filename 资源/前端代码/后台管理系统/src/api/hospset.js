import request from '@/utils/request'

const api_name = '/admin/hosp/hospitalSet'

export default {

    // 医院设置列表
    getHospSetiSetList(current, limit, query) {
        return request({
            url: `${api_name}/batch/${current}/${limit}`,
            method: 'POST',
            data: query
        })
    },
    // 删除医院设置
    deleteHospSet(id) {
        return request({
            url: `${api_name}/${id}`,
            method: "delete",
        });
    },
    // 批量删除
    batchRemoveHospSet(idList) {
        return request({
            url: `${api_name}/batch`,
            method: 'delete',
            data: idList
        })
    },
    // 锁定和取消锁定
    lockHospSet(id, status) {
        return request({
            url: `${api_name}/lock/${id}/${status}`,
            method: 'put'
        })
    },
    // 添加医院设置
    saveHospSet(hospitalSet) {
        return request({
            url: `${api_name}`,
            method: 'post',
            data: hospitalSet
        })
    },
    // 院设置id查询
    getHospSet(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'get'
        })
    },
    //修改医院设置
    updateHospSet(hospitalSet) {
        return request({
            url: `${api_name}`,
            method: 'put',
            data: hospitalSet
        })
    }



}

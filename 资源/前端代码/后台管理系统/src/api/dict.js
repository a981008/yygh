import request from '@/utils/request'

const api_name = '/admin/cmn/dict'

export default {
    dictList(id = 1) {//数据字典列表
      return request ({
        url: `${api_name}/childData/${id}`,
        method: 'get'
      })
    }
  }
  
import axios from 'axios'
import {ElLoading} from 'element-plus'

// let baseURL = 'http://47.95.235.161:8899/management'
let baseURL = 'http://localhost:8899/management'

const $http = axios.create({
    baseURL,
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json',
    }
})

const get = (url, params) => {
    params = params || {}
    return new Promise((resolve, reject) => {
        let loading = ElLoading.service({
            text: '加载中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        })
        $http.get(url, {
            params,
            headers: {
                'Authorization': 'Bearer ' + (window.sessionStorage.getItem('token') || '')
            }
        }).then(res => {
            loading.close()
            let result = res.data
            if (result.errCode === 200) {
                resolve(result.data)
            } else {
                reject(result.errMsg)
                // alert(res.data.errMsg)
            }
        }).catch(error => {
            loading.close()
            reject('网络异常')
            // alert('网络异常')
        })
    })
}

// 静默 GET:不弹全屏 Loading,给轮询等高频请求用(否则每 2 秒闪一次遮罩像"刷新")
const getSilent = (url, params) => {
    params = params || {}
    return new Promise((resolve, reject) => {
        $http.get(url, {
            params,
            headers: {
                'Authorization': 'Bearer ' + (window.sessionStorage.getItem('token') || '')
            }
        }).then(res => {
            let result = res.data
            if (result.errCode === 200) {
                resolve(result.data)
            } else {
                reject(result.errMsg)
            }
        }).catch(error => {
            reject('网络异常')
        })
    })
}

const post = (url, params) => {
    params = params || {}
    return new Promise((resolve, reject) => {
        let loading = ElLoading.service({
            text: '加载中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        })
        $http.post(url, JSON.stringify(params), {
                headers: {
                    'Authorization': 'Bearer ' + (window.sessionStorage.getItem('token') || '')
                }
            }
        ).then(res => {
            loading.close()
            if (res.data.errCode === 200) {
                resolve(res.data.data)
            } else {
                reject(res.data.errMsg)
                // alert(res.data.errMsg)
            }
        }).catch(err => {
            loading.close()
            reject('网络异常')
            // alert('网络异常')
        })
    })
}

// 静默 POST:不弹全屏 Loading,给聊天等即时交互用(否则每问一句闪一次遮罩)
const postSilent = (url, params) => {
    params = params || {}
    return new Promise((resolve, reject) => {
        $http.post(url, JSON.stringify(params), {
            headers: {
                'Authorization': 'Bearer ' + (window.sessionStorage.getItem('token') || '')
            }
        }).then(res => {
            if (res.data.errCode === 200) {
                resolve(res.data.data)
            } else {
                reject(res.data.errMsg)
            }
        }).catch(err => {
            reject('网络异常')
        })
    })
}

export function getBaseUrl() {
    return baseURL
}
//管理员登录
export function apiLogin(params) {
    return post('/user/login', params)
}
//业主登录
export function apiNormalLogin(params) {
    return post('/roomUser/login', params)
}
//修改业主密码
export function apiUpdatePwd(params) {
    return post('/roomUser/updatePwd', params)
}
//管理员添加业主
export function apiAddUser(params) {
    return post('/user/addUser', params)
}
//修改管理员密码
export function apiUpdateUserByUid(params) {
    return post('/user/updateUserByUid', params)
}
//删除业主
export function apiDeleteUserByUid(params) {
    return post('/user/deleteUserByUid', params)
}
//查询管理员信息
export function queryUser() {
    return get('/user/queryUser')
}
//查询业主信息
export function apiQueryRoomUserByCondition(params) {
    return get('/roomUser/queryRoomUserByCondition', params)
}
//添加业主信息
export function apiAddRoomUser(params) {
    return post('/roomUser/addRoomUser', params)
}
//修改业主信息
export function apiUpdateRoomUserById(params) {
    return post('/roomUser/updateRoomUserById', params)
}
//删除业主信息
export function apiDeleteRoomUserById(params) {
    return post('/roomUser/deleteRoomUserById', params)
}
//查询所有业主信息
export function apiQueryRoomUser() {
    return get('/roomUser/queryRoomUser')
}
//添加停车场信息
export function apiAddCarport(params) {
    return post('/carport/addCarport', params)
}
//修改停车场信息
export function apiUpdateCarport(params) {
    return post('/carport/updateCarport', params)
}
//删除停车场信息
export function apiDeleteCarport(params) {
    return post('/carport/deleteCarport', params)
}
//获取所有停车场信息
export function apiGetCarport() {
    return get('/carport/getCarport')
}
//添加车位信息
export function apiAddPark(params) {
    return post('/park/addPark', params)
}
//修改车位信息
export function apiUpdatePark(params) {
    return post('/park/updatePark', params)
}
//删除车位信息
export function apiDeletePark(params) {
    return post('/park/deletePark', params)
}
//获取所有车位信息
export function apiGetPark() {
    return get('/park/getPark')
}
//通过车位编号获取车位信息
export function apiGetParkByNumber(params) {
    return get('/park/getParkByNumber', params)
}
//添加业主车辆信息
export function apiAddOwnerCar(params) {
    return post('/ownerCar/addOwnerCar', params)
}
//修改业主车辆信息
export function apiUpdateOwnerCar(params) {
    return post('/ownerCar/updateOwnerCar', params)
}
//删除业主车辆信息
export function apiDeleteOwnerCar(params) {
    return post('/ownerCar/deleteOwnerCar', params)
}
//获取业主车辆信息
export function apiGetOwnerCar() {
    return get('/ownerCar/getOwnerCar')
}
//通过车牌号获取业主车辆信息
export function getOwnerCarByNumber(params) {
    return get('/ownerCar/getOwnerCarByNumber', params)
}
//通过业主id获取该业主车辆信息
export function apiGetOwnerCarByOwnerId(params) {
    return get('/ownerCar/getOwnerCarByOwnerId', params)
}

//添加返省登记记录
export function apiAddRecord(params) {
    return post('/record/addRecord', params)
}
//修改返省登记记录
export function apiUpdateRecord(params) {
    return post('/record/updateRecord', params)
}
//删除返省登记记录
export function apiDeleteRecord(params) {
    return post('/record/deleteRecord', params)
}
//获取返省登记记录
export function apiGetRecord() {
    return get('/record/getRecord')
}
//模糊查询停车场
export function apiGetCarportByCondition(params) {
    return get('/record/getRecordByCondition', params)
}

//添加租售信息
export function apiAddRent(params) {
    return post('/rent/addRent', params)
}
//更新租售信息
export function apiUpdateRent(params) {
    return post('/rent/updateRent', params)
}
//获取租售信息
export function apiGetRent() {
    return get('/rent/getRent')
}
//通过业主id获取该业主租售信息
export function getRentByOwnerId(params) {
    return get('/rent/getRentByOwnerId',params)
}
//删除租售信息
export function apiDeleteRent(params) {
    return post('/rent/deleteRent', params)
}
//查询租售信息
export function apiSearchRent(params) {
    return get('/rent/getRentByParkNumber', params)
}

//添加公告
export function apiAddNews(params) {
    return post('/clientNews/addNews', params)
}
//修改公告
export function apiUpdateNews(params) {
    return post('/clientNews/updateNews', params)
}
//删除公告
export function apiDeleteNews(params) {
    return post('/clientNews/deleteNews', params)
}
//获取所有公告
export function apiGetNews() {
    return get('/clientNews/getNews')

}
//通过公告id获取公告具体内容
export function apiGetNewsById(params) {
    return get('/clientNews/getNewsById', params)
}
//添加轮播图信息
export function apiAddBanner(params) {
    return post('/clientBanner/addBanner', params)
}
//删除轮播图信息
export function apiDeleteBanner(params) {
    return post('/clientBanner/deleteBanner', params)
}
//修改轮播图信息
export function apiUpdateBanner(params) {
    return post('/clientBanner/updateBanner', params)
}
//获取轮播图信息
export function apiGetBanner() {
    return get('/clientBanner/getBanner')
}

//添加费用信息
export function apiAddCost(params) {
    return post('/payCost/addCost', params)
}
//删除费用信息
export function apiDeleteCost(params) {
    return post('/payCost/deleteCost', params)
}
//修改费用信息
export function apiUpdateCost(params) {
    return post('/payCost/updateCost', params)
}
//获取所有业主的费用账单
export function apiGetCost() {
    return get('/payCost/getCost')
}
//通过业主id获取该用户得费用账单
export function apiGetCostByUid(params) {
    return get('/payCost/getCostByUid', params)
}
//支付费用(包括物业费和停车费)
export function apiPay(params) {
    return post('/payCost/pay', params)
}

//调节停车费每小时多少钱
export function updateParkingCharge(params) {
    return post('/payCost/updateParkingCharge', params)
}
//获取停车费
export function getParkingCharge() {
    return get('/payCost/getParkingCharge')
}
//添加报修记录
export function apiAddFault(params) {
    return post('/fault/addFault', params)
}
//删除报修记录
export function apiDeleteFault(params) {
    return post('/fault/deleteFault', params)
}
//业主修改报修信息
export function apiUpdateFaultForClient(params) {
    return post('/fault/updateFaultForClient', params)
}
//管理员修改报修信息
export function apiUpdateFaultForAdmin(params) {
    return post('/fault/updateFaultForAdmin', params)
}
//管理员获取所有业主的报修信息 
export function apiGetFault() {
    return get('/fault/getFault')
}
//通过业主id获取该业主所有报修信息
export function apiGetFaultByUid(params) {
    return get('/fault/getFaultByUid', params)
}
//添加建议
export function apiAddAdvice(params) {
    return post('/advice/addAdvice', params)
}
//获取所有建议
export function apiGetAdvice() {
    return get('/advice/getAdvice')
}
//创建支付订单(真实支付)
export function apiCreateOrder(params) {
    return post('/payCost/createOrder', params)
}
//查询订单支付状态(前端轮询)
export function apiQueryOrder(params) {
    return getSilent('/payCost/queryOrder', params)
}
//智能客服提问
export function apiChatAsk(params) {
    return postSilent('/chat/ask', params)
}

import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/components/Login";
import Home from "@/components/Home";
import PropertyManage from "@/components/PropertyManage";
import RoomUserManage from "@/components/RoomUserManage";
import EpidemicRecord from "@/components/EpidemicRecord";
import CarportManage from "@/components/CarportManage";
import ParkManage from "@/components/ParkManage";
import OwnerCarManage from "@/components/OwnerCarManage";
import ParkRentManage from "@/components/ParkRentManage";
import RentManage from "@/components/RentManage";
import NormalHome from "@/components/NormalHome";
import ClientNewsInfo from "@/components/ClientNewsInfo";
import ClientMain from "@/components/ClientMain";
import Repairs from "@/components/ClientRepairs";
import PayCost from "@/components/ClientPayCost";
import PayCostManage from "@/components/PayCostManage";
import NewsManage from "@/components/NewsManage";
import ClientBanner from "@/components/BannerManage";
import RepairsManage from "@/components/RepairsManage";
import ComplaintAndAdvice from "@/components/ComplaintAndAdvice";
import AdviceManage from "@/components/AdviceManage";
import ClientRentCar from "@/components/ClientRentCar";
import CarCostManage from "@/components/CarCostManage";


Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        component: Login
    }, {
        path: '/client',
        component: NormalHome,
        children: [
            {path: '/main', component: ClientMain},
            {path: '/repairs', component: Repairs},
            {path: '/pay_cost', component: PayCost},
            {path: '/advice', component: ComplaintAndAdvice},
            {path: '/rent_car', component: ClientRentCar}
        ]
    }, {
        path: '/news_info',
        component: ClientNewsInfo
    },
    {
        path: '/home',
        component: Home,
        children: [
            {path: '/property_manage', component: PropertyManage},
            {path: '/room_user_manage', component: RoomUserManage},
            {path: '/epidemic_record', component: EpidemicRecord},
            {path: '/carport_manage', component: CarportManage},
            {path: '/park_manage', component: ParkManage},
            {path: '/car_cost', component: CarCostManage},
            {path: '/owner_manage', component: OwnerCarManage},
            {path: '/park_rent_manage', component: ParkRentManage, name: 'ParkRentManage'},
            {path: '/rent_manage', component: RentManage},
            {path: '/banner_manage', component: ClientBanner},
            {path: '/news_manage', component: NewsManage},
            {path: '/pay_cost_manage', component: PayCostManage},
            {path: '/repairs_manage', component: RepairsManage},
            {path: '/advice_manage', component: AdviceManage}
        ]
    }

]

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
    if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
    return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
    routes
})

router.beforeEach((to, from, next) => {
    if (to.path === '/login') return next()

    let userid = window.sessionStorage.getItem('userid')
    if (!userid) return next('/login')
    next()
})

export default router

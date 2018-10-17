import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import BasicDefinition from '@/components/BasicDefinition'
import DtoTemplate from '@/components/DtoTemplate'
import ServiceTemplate from '@/components/ServiceTemplate'
import Error from '@/components/Error'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/basic-definition',
      name: 'BasicDefinition',
      component: BasicDefinition
    },
    {
      path: '/dto/:name',
      name: 'DtoTemplate',
      component: DtoTemplate
    },
    {
      path: '/service/:name',
      name: 'ServiceTemplate',
      component: ServiceTemplate
    },
    {
      path: '/*',
      name: 'Error',
      component: Error
    }
  ]
})

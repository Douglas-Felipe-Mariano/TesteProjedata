import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  { 
    path: '/', 
    name: 'Home', 
    component: () => import('../views/Inventory/Inventory.vue') 
  },
  { 
    path: '/products', 
    name: 'Products', 
    component: () => import('../views/Products/Products.vue') 
  },
  { 
    path: '/planning', 
    name: 'Planning', 
    component: () => import('../views/Planning/Planning.vue') 
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
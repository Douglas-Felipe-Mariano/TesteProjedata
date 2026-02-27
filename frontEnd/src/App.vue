<template>
  <nav>
    <router-link to="/">Estoque</router-link> |
    <router-link to="/products">Produtos</router-link> |
    <router-link to="/planning">Otimização de Produção</router-link>
  </nav>
  <router-view /> </template>

<style>
nav { padding: 20px; background: #2c3e50; color: white; margin-bottom: 20px; }
nav a { color: white; margin: 0 10px; text-decoration: none; font-weight: bold; }
nav a.router-link-exact-active { color: #42b983; }
body { font-family: Arial, sans-serif; margin: 0; }
</style>
<template>
    <managerBar />
    <UserManagement v-if="maintainance_function==='userManagement'" />
    <RoleManagment v-if="maintainance_function==='roleAuthManagement'" />

</template>

<script setup>
import managerBar from './AuthBar.vue';

import { provide,ref } from 'vue';
import UserManagement from './userManagement.vue';
import RoleManagment from './RoleManagment.vue';

const maintainance_function = ref("userManagement")
provide("maintainance_function",maintainance_function)

</script>

<style>
@layer reset, primevue, custom;

@layer custom {
}
</style>
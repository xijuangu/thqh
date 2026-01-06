<template>
    <managerBar />
    <UpLoadBoard v-if="maintainance_function==='uploadFile'"/>
    <ClientClassMaintenance v-if="maintainance_function==='clientClassMaintenance'"/>
    <PersonnelManagement v-if="maintainance_function==='PersonnelManagement'"/>
    <DepartmentManagement v-if="maintainance_function==='DepartmentManagement'"/>

</template>

<script setup>
import managerBar from './ManagerBar.vue';
import UpLoadBoard from './UpLoadBoard.vue';
import ClientClassMaintenance from './ClientClassMaintenance.vue';
import PersonnelManagement from './PersonnelManagement.vue';
import DepartmentManagement from './DepartmentManagement.vue';
import { provide,ref } from 'vue';

const maintainance_function = ref("uploadFile")
provide("maintainance_function",maintainance_function)

</script>

<style>
@layer reset, primevue, custom;

@layer custom {
}
</style>
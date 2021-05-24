<template>
  <div class="administration">
      <v-container fluid>
        <router-view></router-view>
      </v-container>
  </div>
</template>

<script>
// @ is an alias to /src
//import AdministrationComponent from '@/components/administration/AdministrationComponent.vue';
//import NavigationDrawer from '@/components/administration/includes/NavigationDrawer.vue'

export default {
  name: 'Administration',
  components: {
    //AdministrationComponent,
    //NavigationDrawer
  },
  data() {
    return {
      showLoginModal: false,
    }
  },
  created() {
    console.log("Set admin true")
    this.$store.state.isAdministration = true;
    //this.$store.dispatch('setAdministration', 'true');
  },
  mounted() {
    
        console.log("Set admin true")
        this.$store.dispatch('setAdministration', true);
    },
}
</script>
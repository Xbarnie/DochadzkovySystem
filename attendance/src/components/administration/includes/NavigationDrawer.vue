<template>
    <v-navigation-drawer clipped permanent app>
          <v-list dense>
        <v-list-item
          v-for="item in items"
          :key="item.title"
          link 
          @click="router(item.link)"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title >{{item.title}}</v-list-item-title>
          </v-list-item-content>
          
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
</template>

<script>
export default {
    name: 'NavigationDrawer',
    data () {
      return {
        items: [
          { title: this.$t('administration.dayLog'), icon: 'mdi-home-city', link: '/administration/' },
          { title: this.$t('administration.attendenceOverview'), icon: 'mdi-home-city', link: '/administration/attendence-overview' },
          { title: this.$t('administration.overviewOfApproaches'), icon: 'mdi-account', link: '/administration/overview-approaches' },
        ],
      }
    },
    methods: {
      router(link) {
        console.log(link);
        this.$router.push(link);
      }
    }
}
</script>
<style>
.v-navigation-drawer__prepend {
  background-color: #486DEE !important;
  border-color: #486DEE !important;
}
</style>
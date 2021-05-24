<template>
  <v-app app>
    <v-navigation-drawer fixed permanent app v-if="$store.state.isAdministration" color="#1d2228" dark>
    <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title" style="white-space: normal;">
            {{$t('employeeAttendance')}}
          </v-list-item-title>
          <v-list-item-subtitle>
            by Marián Lovich
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    <v-divider></v-divider>
      <v-list dense v-if="$store.state.user != null && $store.state.user.role[0].authority == 'Admin'">

        <v-list-item link active-class="highlighted" to="/administration/day-log">
          <v-list-item-icon>
            <v-icon>mdi-home-city</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{$t('administration.dayLog')}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item link active-class="highlighted" to="/administration/attendence-overview">
          <v-list-item-icon>
            <v-icon>mdi-home-city</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{$t('administration.attendenceOverview')}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item link active-class="highlighted" to="/administration/overview-approaches">
          <v-list-item-icon>
            <v-icon>mdi-account</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{$t('administration.overviewOfApproaches')}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item link active-class="highlighted" to="/administration/editing-history">
          <v-list-item-icon>
            <v-icon>mdi-history</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{$t('editingHistory')}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item link active-class="highlighted" @click="closeAdministration">
          <v-list-item-icon>
            <v-icon>mdi-logout-variant</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{$t('logout')}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <v-list dense v-if="$store.state.user != null && $store.state.user.role[0].authority == 'User'">
        <v-list-item
          v-for="item in simpleUserMenu"
          :key="item.title"
          link
          :to="item.link" 
          active-class="highlighted"
          :class="item.link === $route.path ? 'highlighted' : ''"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{item.title}}</v-list-item-title>
          </v-list-item-content>
          
        </v-list-item>
        <v-list-item link active-class="highlighted" @click="closeAdministration">
          <v-list-item-icon>
            <v-icon>mdi-logout-variant</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{$t('logout')}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <template v-slot:append>
        <div class="pa-2 d-flex" style="height: 60px; width: 70px;">
          <v-img width="50px" class="ma-1 pointer" src="@/assets/slovakia.png" @click="slovakLanguage()"></v-img>
          <v-img width="50px" class="ma-1 pointer" src="@/assets/english.jpg" @click="englishLanguage()"></v-img>
        </div>
      </template>
    </v-navigation-drawer>
    <v-app-bar color="#1e88e5" elevate-on-scroll app v-if="$store.state.isAdministration">
       <v-app-bar-nav-icon></v-app-bar-nav-icon>
      <span class="white--text">{{$store.state.toolbar}}</span>
      
    </v-app-bar>

    <v-main class="app-background">
      <router-view></router-view>
    </v-main>

    <v-dialog
      persistent
      v-model="dialog"
      width="500"
    >
    <v-card>
        <v-card-title class="headline">
          {{ $t("homePage.insertWhereAttendenceSystemIsLocated")}}
        </v-card-title>
        <v-card-text>
          <v-form v-model="validRoomName">
            <v-row>
              <v-col>
                <v-text-field
                  v-model="roomName"
                  :rules="roomRules"
                  :counter="10"
                  label="Room name"
                  required
                ></v-text-field>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1"
            text
            :disabled="!validRoomName"
            @click="saveRoomName()"
          >
            {{$t("homePage.insert")}}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script>

import VueJwtDecode from 'vue-jwt-decode';

export default {
  name: 'App',

  data () {
      return {
        dialog: false,
        validRoomName: false,
        roomName: '',
        roomRules: [
          v => !!v || 'Room is required',
          v => v.length <= 10 || 'Room must be less than 10 characters',
        ],
        items: [
          { title: this.$t('administration.dayLog'), icon: 'mdi-home-city', link: '/administration/day-log' },
          { title: this.$t('administration.attendenceOverview'), icon: 'mdi-home-city', link: '/administration/attendence-overview' },
          { title: this.$t('administration.overviewOfApproaches'), icon: 'mdi-account', link: '/administration/overview-approaches' },
          { title: this.$t('editingHistory'), icon: 'mdi-history', link: '/administration/editing-history' },
        ],
        simpleUserMenu: [
          {title: this.$t('administration.dayLog'), icon: 'mdi-home-city', link: '/administration/day-log'}
        ],
      }
    },
    methods: {
      saveRoomName() {
        localStorage.attentence_room = this.roomName;
        if(localStorage.attentence_room != null) {
          this.dialog = false;
        }
      },
      interway() {
        this.$router.push({path: 'interway'})
      },
      router(link) {
        console.log(link);
        this.$router.push(link);
      },
      closeAdministration() {
        console.log("closeAdministration");
        localStorage.removeItem('attendanceToken');
        this.$store.dispatch('setUser', null);
        this.$router.push({path: '/'})
        // Remove login
      },
      slovakLanguage() {
        localStorage.attendanceLanguage = 'sk'
       this.$root.$i18n.locale = 'sk'
    },
    englishLanguage() {
      localStorage.attendanceLanguage = 'en'
      this.$root.$i18n.locale = 'en'
    }
    },
  created() {
    this.$root.$i18n.locale = localStorage.attendanceLanguage || 'sk'
    console.log("Attendence room: ");
    console.log(localStorage.attentence_room);
    if(localStorage.attentence_room == null) {
      this.dialog = true;
    }
    console.log("Administration:");
    console.log(this.$store.state.isAdministration);
    // Get logged user from LocalStora and save him to Store
    if(localStorage.attendanceToken != null) {
      console.log('User is logged');
      console.log(localStorage.attendanceToken);
      // Decode token
      var decoded = VueJwtDecode.decode(localStorage.attendanceToken);
      if(decoded != null) {
        console.log("After lofin");
        console.log(decoded);
        this.$store.dispatch('setUser', decoded);
      }
    }
  },
  beforeMount() {
    console.log("BeforeMount");
  }
};
</script>

<style>
  .app-background {
    background-color: #eff2f5;
    background-image: url('./assets/background1.jpg');
    background-repeat: repeat;
  }
</style>
<template>
  <v-container fill-height>
    <div class="topcorner">
      <v-card style="border-radius: 5px 0 0 5px;">
        <v-card-title>{{date}} {{time}}</v-card-title>
      </v-card>
    </div>
    <div class="bottomcorner d-flex">
      <v-img width="50px" class="ma-1 pointer" src="@/assets/slovakia.png" @click="slovakLanguage()"></v-img>
      <v-img width="50px" class="ma-1 pointer" src="@/assets/english.jpg" @click="englishLanguage()"></v-img>
    </div>
    <v-row justify="center" align="center" >
      <v-col cols="12" md="4">
        <!--Úvodná obrazovka - tlačidlo Zamestnanec */ -->
        <v-card
          @click="employeePage($t('homePage.lunch'))"
          height="100"
          class="center-vh mb-2">
          <h2>{{ $t("employee") }}</h2>
        </v-card>
      </v-col>
      <v-col cols="12" md="4">
        <v-card
          @click="administratorPage($t('homePage.lunch'))"
          height="100"
          class="center-vh mb-2"
        >
          <h2>{{ $t("administrator") }}</h2>
        </v-card>
      </v-col>
    </v-row>
    <v-dialog v-model="showLoginModal" width="500">
      <v-card>
        <v-card-title class="headline grey lighten-2">
          {{ $t("pleaseLogIn") }}
        </v-card-title>

        <v-card-text>
          <v-container>
            <v-row>
              <v-col lg="12">
                <v-text-field
                  v-model="email"
                  label="E-mail*"
                  required
                ></v-text-field>
              </v-col>
              <v-col lg="12">
                <v-text-field
                  v-model="password"
                  :label="$t('password')"
                  required
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPassword ? 'text' : 'password'"
                  @click:append="showPassword = !showPassword"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="login()">
            {{ $t("logIn") }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
// @ is an alias to /src
import VueJwtDecode from 'vue-jwt-decode';

export default {
  name: "Home",
  data() {
    return {
      showLoginModal: false,
      email: "",
      password: "",
      showPassword: false,
      date: null,
      time: null
    };
  },
  methods: {
    employeePage() {
      this.$router.push("/employee");
    },
    administratorPage() {
      console.log("Administration");
      // Login before access administration
      if (localStorage.attendanceToken == null) {
        this.showLoginModal = true;
      } else {
        this.$router.push("/administration/day-log");
      }
    },
    login() {
            console.log("login");
            if(this.email != '' && this.password != '') {
                console.log("HERE");
                this.$axios.post('/login', {email: this.email, password: this.password}).then((response) => {
                    if(response.data != null) {
                        console.log(response);
                        if(response.data.accessToken != null) {
                            console.log(response.data.accessToken);
                            localStorage.attendanceToken = response.data.accessToken;
                            // Set store
                            var decoded = VueJwtDecode.decode(localStorage.attendanceToken);
                            this.$store.dispatch('setUser', decoded);
                            this.$router.push("/administration/day-log");
                        }
                    }
                });
            } else {
                console.log("OPPS")
            }
        },
    parseDate(datetime) {
                    const tzoffset = new Date().getTimezoneOffset() * 60000; //offset in milliseconds
                    const datetimeISO = new Date(datetime - tzoffset).toISOString().slice(0, -1);

                    let datetimeArr = datetimeISO.split("T");
                    let dateArr = datetimeArr[0].split("-");
                    dateArr.reverse();
                    const date = dateArr.join(".");
                    return date;
    },
    parseTime(datetime) {
                    const tzoffset = new Date().getTimezoneOffset() * 60000; //offset in milliseconds
                    const datetimeISO = new Date(datetime - tzoffset).toISOString().slice(0, -1);

                    let datetimeArr = datetimeISO.split("T");
                    let timeArr = datetimeArr[1].split(":");
                    let seconds = timeArr[2].split(".");
                    timeArr.pop();
                    const time = timeArr.join(":");
                    return time + ":"+seconds[0];
    },
    slovakLanguage() {
      localStorage.attendanceLanguage = 'sk'
      this.$i18n.locale = 'sk'
    },
    englishLanguage() {
      localStorage.attendanceLanguage = 'en'
      this.$i18n.locale = 'en'
    }
  },
  mounted() {
        console.log("Set admin false")
        this.$store.dispatch('setAdministration', false);
        const self = this;
        this.date = self.parseDate(new Date());
        this.time = self.parseTime(new Date());
        setInterval(() => {
          this.date = self.parseDate(new Date());
          this.time = self.parseTime(new Date());
        }, 1000); // refresh kazdu sekundu
    },
  created() {
  },
};
</script>

<style>
.topcorner{
   position:absolute;
   top:0;
   right:0;
  }
.bottomcorner {
  position:absolute;
   bottom:0;
   right:0;
}
.pointer {
  cursor: pointer;
}
</style>

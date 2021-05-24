<template>
  <v-container>
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
      <v-col cols="3">
        <v-card
          @click="goBack()"
          height="100"
          class="center-vh mb-2"
          :disabled="firstButtonsDisabled"
        >
          <v-icon>mdi-arrow-left</v-icon>
          <h2 class="ml-3">{{ $t("back") }}</h2>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6" md="3" offset-md="2">
        <v-card
          @click="selectFirst($t('homePage.lunch'))"
          height="100"
          class="center-vh mb-2"
          :disabled="firstButtonsDisabled"
        >
          <h2>{{ $t("homePage.lunch") }}</h2>
        </v-card>
        <v-card
          @click="selectFirst($t('homePage.doctor'))"
          height="100"
          class="center-vh mb-2"
          :disabled="firstButtonsDisabled"
        >
          <h2>{{ $t("homePage.doctor") }}</h2>
        </v-card>
        <v-card
          @click="selectFirst($t('homePage.businessTrip'))"
          height="100"
          class="center-vh mb-2"
          :disabled="firstButtonsDisabled"
        >
          <h2>{{ $t("homePage.businessTrip") }}</h2>
        </v-card>
        <v-card
          @click="selectFirst($t('homePage.privateTrip'))"
          height="100"
          class="center-vh mb-2"
          :disabled="firstButtonsDisabled"
        >
          <h2>{{ $t("homePage.privateTrip") }}</h2>
        </v-card>
        <v-card
          @click="selectFirst($t('homePage.vacation'))"
          height="100"
          class="center-vh mb-2"
          :disabled="firstButtonsDisabled"
        >
          <h2>{{ $t("homePage.vacation") }}</h2>
        </v-card>
      </v-col>
      <v-col cols="6" md="3" offset-md="2">
        <v-card
          @click="selectSecond('arrival')"
          height="100"
          class="center-vh  mb-2"
          :disabled="secondButtonsDisabled"
        >
          <h2>{{ $t("homePage.arrival") }}</h2>
        </v-card>
        <v-card
          @click="selectSecond('leaving')"
          height="100"
          class="center-vh  mb-2"
          :disabled="secondButtonsDisabled"
        >
          <h2>{{ $t("homePage.leaving") }}</h2>
        </v-card>
      </v-col>
    </v-row>
    <!--<v-row>
            <v-col>
                <v-card @click="administration()" height="100" class="center-vh">
                    <span>{{$t("homePage.administration")}}</span>
                </v-card>
            </v-col>
        </v-row> -->

    <v-dialog v-model="dialog.open" persistent max-width="290">
      <v-card>
        <v-card-title class="headline">
          {{ dialog.text }}
        </v-card-title>
        <v-card-text class="text--primary">
          <div>{{ dialog.text1 }}</div>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="cancelAction()">
            {{ $t("homePage.cancel") }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
    <v-dialog v-model="attachCardDialog" persistent max-width="400">
      <v-card>
        <v-card-title class="headline">
          Zvolili ste  <span class="ml-1 mr-1" v-if="dialog.text1 != ''"> {{dialog.text1}} -></span> {{dialog.text2}}
        </v-card-title>
        <v-card-text
          >Prosím priložte čipovú kartu a počkajte kým dialógové okno
          zmizne.</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1"
            text
            @click="cancelAction()"
          >
            {{$t('cancel')}}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog
      v-model="errorDialog.open"
      persistent
      max-width="290"
      hide-overlay
    >
      <v-card color="error">
        <v-card-title>
          {{$t('error')}}!
        </v-card-title>
        <v-card-text>
          {{ errorDialog.message }}
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog
      v-model="successDialog.isOpen"
      persistent
      max-width="290"
      hide-overlay
    >
      <v-card color="success">
        <v-card-title>
          {{successDialog.name}} - {{successDialog.type}}
        </v-card-title>
        <v-card-text>
          <h3>Čas: {{successDialog.time}}</h3>
          
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import VueJwtDecode from "vue-jwt-decode";

export default {
  name: "EmployeePage",
  data() {
    return {
      valid: false,
      nazov: "",
      report: "",
      attachCardDialog: false,
      dialog: {
        open: false,
        text: "",
        text1: "",
        text2: ""
      },
      showLoginModal: false,
      email: "",
      password: "",
      showPassword: false,
      selectedFirst: null,
      selectedSecond: null,
      firstButtonsDisabled: false,
      secondButtonsDisabled: false,
      errorDialog: {
        open: false,
        message: ""
      },
      date: null,
      time: null,
      successDialog: {
        isOpen: false,
        name: null,
        time: null,
        type: null
      }
    };
  },
  methods: {
    administration() {
      console.log("Administration");
      // Login before access administration
      if (localStorage.attendanceToken == null) {
        this.showLoginModal = true;
      } else {
        this.$router.push({ path: "administration" });
      }
    },
    cancelAction() {
      this.secondButtonsDisabled= false;
      this.dialog.text1 = '';
      this.dialog.text2 = '';
      var json = {
        room: localStorage.attentence_room,
        token: null
      };
      this.$axios
        .post("/api/main/remove-action", json)
        .then(response => {
          console.log(response);
          if (response.data != null) {
            console.log("Canceled", response.data);
            this.dialog.open = false;
            this.attachCardDialog = false;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    login() {
      console.log("login");
      if (this.email != "" && this.password != "") {
        console.log("HERE");
        this.$axios
          .post("/login", { email: this.email, password: this.password })
          .then(response => {
            if (response.data != null) {
              console.log(response);
              if (response.data.accessToken != null) {
                console.log(response.data.accessToken);
                localStorage.attendanceToken = response.data.accessToken;
                // Set store
                var decoded = VueJwtDecode.decode(localStorage.attendanceToken);
                this.$store.dispatch("setUser", decoded);
                this.$router.push({ path: "administration" });
              }
            }
          });
      } else {
        console.log("OPPS");
      }
    },
    selectFirst(selected) {
      this.successDialog.type = selected;
      this.dialog.text1 = selected;
      this.firstButtonsDisabled = true;
      console.log("selectFirst", selected);
      this.selectedFirst = selected;
    },
    selectSecond(selected) {
      if(selected === 'arrival'){
        this.dialog.text2 = "Príchod"
      }
      if(selected === 'leaving') {
        this.dialog.text2 = "Odchod"
      }
      
      this.secondButtonsDisabled = true;
      this.attachCardDialog = true;
      var apiURL;
      if (this.selectedFirst != null) {
        console.log("Fist is selected too");
        if (this.selectedFirst === "Obed") {
          apiURL = "/api/main/lunch" + "-" + selected;
        }
        if (this.selectedFirst === "Lekár") {
          apiURL = "/api/main/doctor" + "-" + selected;
        }
        if (this.selectedFirst === "Služobná cesta") {
          apiURL = "/api/main/businesstrip" + "-" + selected;
        }
        if (this.selectedFirst === "Súkromná cesta") {
          apiURL = "/api/main/privatetrip" + "-" + selected;
        }
        if (this.selectedFirst === "Dovolenka") {
          apiURL = "/api/main/vacation" + "-" + selected;
        }
        this.selectedFirst = null;
      } else {
         this.dialog.text1 = ''
        console.log(selected);
        apiURL = "/api/main/" + selected;
      }
      console.log(apiURL);

      this.$axios
        .get(apiURL + "/" + localStorage.attentence_room)
        .then(response => {
          console.log(response);
          this.firstButtonsDisabled = false;
          this.secondButtonsDisabled = false;
          this.attachCardDialog = false;
          if (response.data != null) {
            console.log("successResponse", response.data);
            if (response.data.errorMessage != null) {
              this.errorDialog.message = response.data.errorMessage;
              this.errorDialog.open = true;
              setTimeout(() => {
                this.errorDialog.open = false;
              }, 2500);
            } else {
              if(response.data.employeeName != null) {
                this.successDialog.time = this.$moment(response.data.time).format("HH:mm:ss");
                this.successDialog.name = response.data.employeeName
                this.successDialog.type = response.data.type
                this.successDialog.isOpen = true
                //this.dialog.open = false;
                setTimeout(() => {
                  this.dialog.open = false;
                  this.successDialog.isOpen = false
                }, 2500);
              }
            }
          }
        })
        .catch(error => {
          console.log("error", error);
          this.firstButtonsDisabled = false;
          this.secondButtonsDisabled = false;
          this.attachCardDialog = false;
          // TODO Error dialog
        });
    },
    goBack() {
      this.$router.push("/");
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
    console.log("Set admin false");
    this.$store.dispatch("setAdministration", false);
    const self = this;
        this.date = self.parseDate(new Date());
        this.time = self.parseTime(new Date());
        setInterval(() => {
          this.date = self.parseDate(new Date());
          this.time = self.parseTime(new Date());
        }, 1000); // refresh kazdu sekundu
  },
  created() {
    console.log("Set admin false");
    //this.$store.dispatch('setAdministration', 'true');
  }
};
</script>

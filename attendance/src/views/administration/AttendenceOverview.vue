<template>
  <div>
    <v-row>
      <v-col cols="6">
        <v-card>
          <v-card-title>
            {{ $t("administration.attendenceOverview") }}
          </v-card-title>
          <v-card-text>
            <div class="text--primary">
              <v-row>
                <v-col  cols="6">
                  {{ $t("employee") }}
                </v-col>
                <v-col  cols="6">
                  <v-combobox 
                  solo
                  v-model="filter.selectedEmployee"
                  :items="employees"
                  item-text="firstName"
                  item-value="id"
                  hide-selected
                  dense
                  :label="$t('employee')"
                  >
                    <template slot="item" slot-scope="data">
                      {{ data.item.firstName }} {{ data.item.lastName }} -
                      {{ data.item.department.name }}
                    </template>
                    <template slot="selection" slot-scope="data">
                      {{ data.item.firstName }} {{ data.item.lastName }} -
                      {{ data.item.department.name }}
                    </template>
                  </v-combobox>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="6">
                  {{ $t("department") }}
                </v-col>
                <v-col cols="6">
                  <v-select
                    v-model="filter.department"
                    :items="departments"
                    item-text="name"
                    item-value="id"
                    :label="$t('department')"
                    return-object
                  ></v-select>
                </v-col>
              </v-row>
              <v-row>
                <v-col  cols="6">
                  {{ $t("arrivalDate") }}
                </v-col>
                <v-col  cols="6">
                  <v-menu
                    v-model="arrivalDateDateMenu"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    max-width="290px"
                    min-width="290px"
                  >
                    <template v-slot:activator="{ on, attrs }">
                      <v-text-field
                        v-model="filter.arrivalDate"
                        :label="$t('arrivalDate')"
                        prepend-icon="mdi-calendar"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                      ></v-text-field>
                    </template>
                    <v-date-picker
                        v-model="filter.arrivalDate"
                        locale="sk"
                        @input="arrivalDateDateMenu = false"
                      > </v-date-picker>
                  </v-menu>
                </v-col>
                <v-col cols="12">
                  <v-row>
                      <v-col  cols="6">
                        {{ $t("leaveDate") }}
                      </v-col>
                      <v-col  cols="6">
                        <v-menu
                          v-model="departureDateDateMenu"
                          :close-on-content-click="false"
                          :nudge-right="40"
                          transition="scale-transition"
                          offset-y
                          max-width="290px"
                          min-width="290px"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                              v-model="filter.leaveDate"
                              :label="$t('leaveDate')"
                              prepend-icon="mdi-calendar"
                              readonly
                              v-bind="attrs"
                              v-on="on"
                            ></v-text-field>
                          </template>
                          <v-date-picker
                              v-model="filter.leaveDate"
                              locale="sk"
                              @input="departureDateDateMenu = false"
                            > </v-date-picker>
                        </v-menu>
                      </v-col>
                  </v-row>
                </v-col>
                <v-col cols="12">
                  <v-row>
                    <v-col>
                      <v-menu
                        ref="menu"
                        v-model="arrivalFromMenu"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        :return-value.sync="filter.arrivalFrom"
                        transition="scale-transition"
                        offset-y
                        max-width="290px"
                        min-width="290px"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="filter.arrivalFrom"
                            :label="$t('arrivalFrom')"
                            prepend-icon="mdi-clock-time-four-outline"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-time-picker
                          v-if="arrivalFromMenu"
                          v-model="filter.arrivalFrom"
                          full-width
                          format="24hr"
                          @click:minute="$refs.menu.save(filter.arrivalFrom)"
                        ></v-time-picker>
                      </v-menu>
                    </v-col>
                    <v-col>
                      <v-menu
                        ref="menu1"
                        v-model="arrivalToMenu"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        :return-value.sync="filter.arrivalTo"
                        transition="scale-transition"
                        offset-y
                        max-width="290px"
                        min-width="290px"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="filter.arrivalTo"
                            :label="$t('arrivalTo')"
                            prepend-icon="mdi-clock-time-four-outline"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-time-picker
                          v-if="arrivalToMenu"
                          v-model="filter.arrivalTo"
                          full-width
                          format="24hr"
                          @click:minute="$refs.menu1.save(filter.arrivalTo)"
                        ></v-time-picker>
                      </v-menu>
                    </v-col>
                    <v-col>
                      <v-menu
                        ref="menu2"
                        v-model="departureFromMenu"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        :return-value.sync="filter.departureFrom"
                        transition="scale-transition"
                        offset-y
                        max-width="290px"
                        min-width="290px"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="filter.departureFrom"
                            :label="$t('departureFrom')"
                            prepend-icon="mdi-clock-time-four-outline"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-time-picker
                          v-if="departureFromMenu"
                          v-model="filter.departureFrom"
                          full-width
                          format="24hr"
                          @click:minute="$refs.menu2.save(filter.departureFrom)"
                        ></v-time-picker>
                      </v-menu>
                    </v-col>
                    <v-col>
                      <v-menu
                        ref="menu3"
                        v-model="departureToMenu"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        :return-value.sync="filter.departureTo"
                        transition="scale-transition"
                        offset-y
                        max-width="290px"
                        min-width="290px"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="filter.departureTo"
                            :label="$t('departureTo')"
                            prepend-icon="mdi-clock-time-four-outline"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-time-picker
                          v-if="departureToMenu"
                          v-model="filter.departureTo"
                          full-width
                          format="24hr"
                          @click:minute="$refs.menu3.save(filter.departureTo)"
                        ></v-time-picker>
                      </v-menu>
                    </v-col>
                  </v-row>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-btn @click="getOvierviewSelection()" color="#1e88e5" class="white--text">{{$t('viewReport')}}</v-btn>
                  <v-btn @click="clearFilter()" class="ml-2">{{$t('clearFilter')}}</v-btn>
                </v-col>
              </v-row>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-data-table
          :headers="attendanceHeader"
          :items="attendance"
          :items-per-page="20"
          class="elevation-1"
        >
          <template v-slot:[`item.user`]="{ item }">
            <!--<span
              ><router-link
                :to="{ name: 'UserAttendance', params: { id: item.user.id } }"
                >{{ item.user.firstName }} {{ item.user.lastName }}</router-link
              ></span
            > -->
            <a @click="userInfoModal(item.user.id)" class="text-decoration-none">{{ item.user.firstName }} {{ item.user.lastName }}</a>
          </template>
          <template v-slot:[`item.department`]="{ item }">
            <span>{{ item.user.department.name }}</span>
          </template>
          <template v-slot:[`item.day`]="{ item }">
            <span>{{ (getDayName(item.dateAt)) }}</span>
          </template>
          <template v-slot:[`item.arrivalAt`]="{ item }">
            <span v-if="item.arrivalAt != null">{{
              item.arrivalAt | moment("DD.MM.YYYY HH:mm")
            }}</span>
          </template>
          <template v-slot:[`item.leaveAt`]="{ item }">
            <span v-if="item.leaveAt != null">{{
              item.leaveAt | moment("DD.MM.YYYY HH:mm")
            }}</span>
          </template>
          <template v-slot:[`item.lunchAt`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.lunchFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.lunchFrom != null">{{item.lunchFrom }} -</span><span v-if="item.lunchTo != null">{{item.lunchTo }}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.doctor`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.doctorFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.doctorFrom != null">{{item.doctorFrom }} -</span><span v-if="item.doctorTo != null">{{item.doctorTo }}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.businessFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.businessFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.businessFrom != null">{{item.businessFrom }} -</span><span v-if="item.businessTo != null">{{item.businessTo }}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.privateFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.privateFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.privateFrom != null">{{item.privateFrom}} -</span><span v-if="item.privateTo != null">{{item.privateTo }}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.vacationFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.vacationFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.vacationFrom != null">{{item.vacationFrom }} -</span><span v-if="item.vacationTo != null">{{item.vacationTo }}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.complet`]="{ item }">
            <span> {{ completedTime(item.arrivalAt, item.leaveAt) }}</span>
          </template>
          <template v-slot:[`item.worked`]="{ item }">
            <span v-if="item.doctor != null"> {{ workedTime(item) }}</span>
            <span v-else> {{ workedTime(item) }}</span>
          </template>
        </v-data-table>
      </v-col>
    </v-row>
    <v-dialog v-model="userInfo" width="1000px">
      
      <v-card v-if="selectedUser != null">
        <v-card-title>
          <v-row>
            <v-col cols="12">
              <h2>Osobný výkaz</h2>
            </v-col>
            <v-col >
              <span>{{selectedUser.user.firstName}} {{selectedUser.user.lastName}}</span> <span class="ml-2">({{selectedUser.user.department.name}})</span>
            </v-col>
            <v-col class="pb-0">
              <span  style="float: right;">E-mail: {{selectedUser.user.email}}</span>
            </v-col>
            <v-col cols="12" class="pt-0">
              <span>ID: {{selectedUser.user.id}}</span>
            </v-col>
          </v-row>
        </v-card-title>
        <v-card-text>
          <v-data-table
          :headers="selectedUserHeader"
          :items="selectedUser.attendance"
          :items-per-page="200"
          class="elevation-1"
        >
        <template v-slot:[`item.dateAt`]="{ item }">
            <span>{{ (getDayName(item.dateAt)) }}</span>
          </template>
          <template v-slot:[`item.arrivalAt`]="{ item }">
            <span v-if="item.arrivalAt != null">{{
              item.arrivalAt | moment("DD.MM.YYYY HH:mm")
            }}</span>
          </template>
          <template v-slot:[`item.leaveAt`]="{ item }">
            <span v-if="item.leaveAt != null">{{
              item.leaveAt | moment("DD.MM.YYYY HH:mm")
            }}</span>
          </template>
          <template v-slot:[`item.lunchAt`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.lunchFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.lunchFrom != null">{{item.lunchFrom | moment("HH:mm")}} -</span><span v-if="item.lunchTo != null">{{item.lunchTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.doctor`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.doctorFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.doctorFrom != null">{{item.doctorFrom | moment("HH:mm")}} -</span><span v-if="item.doctorTo != null">{{item.doctorTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.businessFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.businessFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.businessFrom != null">{{item.businessFrom | moment("HH:mm")}} -</span><span v-if="item.businessTo != null">{{item.businessTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.privateFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.privateFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.privateFrom != null">{{item.privateFrom | moment("HH:mm")}} -</span><span v-if="item.privateTo != null">{{item.privateTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.vacationFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.vacationFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.vacationFrom != null">{{item.vacationFrom | moment("HH:mm")}} -</span><span v-if="item.vacationTo != null">{{item.vacationTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.complet`]="{ item }">
            <span> {{ completedTime(item.arrivalAt, item.leaveAt) }}</span>
          </template>
          <template v-slot:[`item.worked`]="{ item }">
            <span v-if="item.doctor != null"> {{ workedTime(item) }}</span>
            <span v-else> {{ workedTime(item) }}</span>
          </template>
          </v-data-table>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: "AttendenceOverview",
  components: {},
  data() {
    return {
      attendance: [],
      departments: [],
      selectedEmployee: null,
      employees: [],
      period: {
        date: new Date().toISOString().substr(0, 7),
        modal: false,
      },
      filter: {
        selectedEmployee: null,
        department: null,
        arrivalFrom: null,
        arrivalTo: null,
        departureFrom: null,
        departureTo: null,
        arrivalDepartureDate: null,
        arrivalDate: null,
        leaveDate: null
      },
      arrivalFromMenu: false,
      arrivalToMenu: false,
      departureFromMenu: false,
      departureToMenu: false,
      arrivalDateDateMenu: false,
      departureDateDateMenu: false,
      userInfo: false,
      selectedUser: null
    };
  },
  mounted() {
    this.$store.dispatch('setToolbar', "administration.attendenceOverview");
    this.getAttendance();
    this.getDepartments();
    this.getUsers();
  },
  methods: {
    getAttendance() {
      this.$axios
        .get("/api/attendance/")
        .then((response) => {
          //console.log("attendance",response);
          this.attendance = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getDepartments() {
      this.$axios
        .get("/api/department/")
        .then((response) => {
          //console.log("departments", response);
          this.departments = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getUsers() {
      this.$axios.get("/api/user/").then((response) => {
        //console.log("All users", response);
        if (response.data != null) {
          this.employees = response.data;
        }
      });
    },
    getOvierviewSelection() {
      console.log("getOvierviewSelection", this.filter)
      var request = {
        userId: null,
        departmentId: null,
        arrivalFrom: this.filter.arrivalFrom,
        arrivalTo: this.filter.arrivalTo,
        departureFrom: this.filter.departureFrom,
        departureTo: this.filter.departureTo,
        arrivalDate: this.filter.arrivalDate,
        leaveDate: this.filter.leaveDate,
      }
      if(this.filter.selectedEmployee != null) {
        request.userId = this.filter.selectedEmployee.id
      }
      if( this.filter.department != null) {
        request.departmentId = this.filter.department.id
      }
      console.log(request);
      this.$axios.post("/api/attendance/filter", request).then((response) => {
        console.log("Attendance Filter", response);
        this.attendance = response.data;
      });
    },
    clearFilter() {
      this.filter.selectedEmployee = null;
      this.filter.department = null,
      this.filter.arrivalFrom = null,
      this.filter.arrivalTo = null,
      this.filter.departureFrom = null,
      this.filter.departureTo = null,
      this.filter.arrivalDate = null,
      this.filter.leaveDate = null,
      this.getAttendance();
    },
    completedTime(time1, time2) {
      var date1 = this.$moment(time1);
      var date2 = this.$moment(time2);
      var minutesDiff = date2.diff(date1, 'minutes');

      var hours = (minutesDiff / 60);
      var rhours = Math.floor(hours);
      var minutes = (hours - rhours) * 60;
      var rminutes = Math.round(minutes);
      return rhours + "h "+ rminutes +"m";
    },
    workedTime(attendance) {
      //console.log("attendance", attendance);
      var lunchDiff = null;
      var doctorDiff = null;
      var businessDiff = null;
      var privateDiff = null;
      var vacationDiff = null;
      var workFrom = this.$moment(attendance.arrivalAt);
      var workTo = this.$moment(attendance.leaveAt);
      var minutesDiff = workTo.diff(workFrom, 'minutes');
      if(attendance.lunchFrom != null && attendance.lunchTo != null) {
        var lunchFrom = this.$moment(attendance.lunchFrom, "HH:mm");
        var lunchTo = this.$moment(attendance.lunchTo, "HH:mm");
        lunchDiff = lunchTo.diff(lunchFrom, 'minutes');
        minutesDiff = minutesDiff-lunchDiff;
        console.log("lunchDiff", lunchDiff)
      }
      if(attendance.doctorFrom != null && attendance.doctorTo != null) {
          var doctorFrom = this.$moment(attendance.doctorFrom, "HH:mm");
          var doctorTo = this.$moment(attendance.doctorTo, "HH:mm");
          doctorDiff = doctorTo.diff(doctorFrom, 'minutes');
          minutesDiff = minutesDiff - doctorDiff;
          console.log('doctorFrom', doctorFrom)
          console.log("DoctorDiff", doctorDiff)
      }
      if(attendance.businessFrom != null && attendance.businessTo != null) {
          var businessFrom = this.$moment(attendance.businessFrom, "HH:mm");
          var businessTo = this.$moment(attendance.businessTo, "HH:mm");
          businessDiff = businessTo.diff(businessFrom, 'minutes');
          minutesDiff = minutesDiff - businessDiff;
          console.log("businessDiff", businessDiff)
      }
      if(attendance.privateFrom != null && attendance.privateTo != null) {
          var privateFrom = this.$moment(attendance.privateFrom, "HH:mm");
          var privateTo = this.$moment(attendance.privateTo, "HH:mm");
          privateDiff = privateTo.diff(privateFrom, 'minutes');
          minutesDiff = minutesDiff - privateDiff;
          console.log("privateDiff", privateDiff)
      }
      if(attendance.vacationFrom != null && attendance.vacationTo != null) {
          var vacationFrom = this.$moment(attendance.vacationFrom, "HH:mm");
          var vacationTo = this.$moment(attendance.vacationTo, "HH:mm");
          vacationDiff = vacationTo.diff(vacationFrom, 'minutes');
          minutesDiff = minutesDiff - vacationDiff;
          console.log("vacationDiff", vacationDiff)
      }
      if(minutesDiff < 0) {
          return "0h 0m";
      }
      var hours = (minutesDiff / 60);
      var rhours = Math.floor(hours);
      var minutes = (hours - rhours) * 60;
      var rminutes = Math.round(minutes);
      return rhours + "h "+ rminutes +"m";
    },
    getDayName(date) {
      var dt = this.$moment(date);
      
      return this.$t(dt.format('dddd'));
    },
    async userInfoModal(userId){
      console.log("Click", userId)
      await this.$axios.get("/api/attendance/user/"+userId).then((response) => {
        console.log("Attendance For User", response);
        if(response.status === 200 ) {
          this.selectedUser = response.data
          this.userInfo = true
        }
      });
    }
  },
  computed: {
      attendanceHeader() {
        return [
        {text: this.$t("name"), align: "start",  sortable: true, value: "user",},
        { text: this.$t("department"), value: "department" },
        { text: this.$t("day"), value: "day" },
        { text: this.$t("arrival"), value: "arrivalAt" },
        { text: this.$t("leaving"), value: "leaveAt" },
        { text: this.$t("lunch"), value: "lunchAt" },
        { text: this.$t("doctor"), value: "doctor" },
        { text: this.$t('homePage.businessTrip'), value: 'businessFrom'},
        { text: this.$t('homePage.privateTrip'), value: 'privateFrom'},
        { text: this.$t('homePage.vacation'), value: 'vacationFrom'},
        { text: this.$t("complet"), value: "complet"},
        { text: this.$t('worked'), value: "worked"}
      ]
      },
      selectedUserHeader() {
        return [
        { text: this.$t("day"), value: "dateAt" },
        { text: this.$t("arrival"), value: "arrivalAt" },
        { text: this.$t("leaving"), value: "leaveAt" },
        { text: this.$t("lunch"), value: "lunchAt" },
        { text: this.$t("doctor"), value: "doctor" },
        { text: this.$t('homePage.businessTrip'), value: 'businessFrom'},
        { text: this.$t('homePage.privateTrip'), value: 'privateFrom'},
        { text: this.$t('homePage.vacation'), value: 'vacationFrom'},
        { text: this.$t("complet"), value: "complet"},
        { text: this.$t('worked'), value: "worked"}
      ]
      }
  },
};
</script>

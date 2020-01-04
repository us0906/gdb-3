<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.systeminstanz.home.createOrEditLabel" v-text="$t('gdb3App.systeminstanz.home.createOrEditLabel')">Create or edit a Systeminstanz</h2>
                <div>
                    <div class="form-group" v-if="systeminstanz.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="systeminstanz.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.bezeichnung')" for="systeminstanz-bezeichnung">Bezeichnung</label>
                        <input type="text" class="form-control" name="bezeichnung" id="systeminstanz-bezeichnung"
                            :class="{'valid': !$v.systeminstanz.bezeichnung.$invalid, 'invalid': $v.systeminstanz.bezeichnung.$invalid }" v-model="$v.systeminstanz.bezeichnung.$model"  required/>
                        <div v-if="$v.systeminstanz.bezeichnung.$anyDirty && $v.systeminstanz.bezeichnung.$invalid">
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.bezeichnung.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.bezeichnung.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 1 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.bezeichnung.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 200 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.geraetNummer')" for="systeminstanz-geraetNummer">Geraet Nummer</label>
                        <input type="text" class="form-control" name="geraetNummer" id="systeminstanz-geraetNummer"
                            :class="{'valid': !$v.systeminstanz.geraetNummer.$invalid, 'invalid': $v.systeminstanz.geraetNummer.$invalid }" v-model="$v.systeminstanz.geraetNummer.$model"  required/>
                        <div v-if="$v.systeminstanz.geraetNummer.$anyDirty && $v.systeminstanz.geraetNummer.$invalid">
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.geraetNummer.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.geraetNummer.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 1 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.geraetNummer.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 200 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.geraetBaujahr')" for="systeminstanz-geraetBaujahr">Geraet Baujahr</label>
                        <input type="text" class="form-control" name="geraetBaujahr" id="systeminstanz-geraetBaujahr"
                            :class="{'valid': !$v.systeminstanz.geraetBaujahr.$invalid, 'invalid': $v.systeminstanz.geraetBaujahr.$invalid }" v-model="$v.systeminstanz.geraetBaujahr.$model"  required/>
                        <div v-if="$v.systeminstanz.geraetBaujahr.$anyDirty && $v.systeminstanz.geraetBaujahr.$invalid">
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.geraetBaujahr.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.geraetBaujahr.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 4 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.systeminstanz.geraetBaujahr.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 4 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.gueltigBis')" for="systeminstanz-gueltigBis">Gueltig Bis</label>
                        <div class="input-group">
                            <input id="systeminstanz-gueltigBis" type="date" class="form-control" name="gueltigBis"  :class="{'valid': !$v.systeminstanz.gueltigBis.$invalid, 'invalid': $v.systeminstanz.gueltigBis.$invalid }"
                            v-model="$v.systeminstanz.gueltigBis.$model"  />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.gwe')" for="systeminstanz-gwe">Gwe</label>
                        <div>
                            <img v-bind:src="'data:' + systeminstanz.gweContentType + ';base64,' + systeminstanz.gwe" style="max-height: 100px;" v-if="systeminstanz.gwe" alt="systeminstanz image"/>
                            <div v-if="systeminstanz.gwe" class="form-text text-danger clearfix">
                                <span class="pull-left">{{systeminstanz.gweContentType}}, {{byteSize(systeminstanz.gwe)}}</span>
                                <button type="button" v-on:click="clearInputImage('gwe', 'gweContentType', 'file_gwe')" class="btn btn-secondary btn-xs pull-right">
                                    <font-awesome-icon icon="times"></font-awesome-icon>
                                </button>
                            </div>
                            <input type="file" ref="file_gwe" id="file_gwe" v-on:change="setFileData($event, systeminstanz, 'gwe', true)" accept="image/*" v-text="$t('entity.action.addimage')"/>
                        </div>
                        <input type="hidden" class="form-control" name="gwe" id="systeminstanz-gwe"
                            :class="{'valid': !$v.systeminstanz.gwe.$invalid, 'invalid': $v.systeminstanz.gwe.$invalid }" v-model="$v.systeminstanz.gwe.$model" />
                        <input type="hidden" class="form-control" name="gweContentType" id="systeminstanz-gweContentType"
                            v-model="systeminstanz.gweContentType" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.bemerkung')" for="systeminstanz-bemerkung">Bemerkung</label>
                        <textarea class="form-control" name="bemerkung" id="systeminstanz-bemerkung"
                            :class="{'valid': !$v.systeminstanz.bemerkung.$invalid, 'invalid': $v.systeminstanz.bemerkung.$invalid }" v-model="$v.systeminstanz.bemerkung.$model" ></textarea>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.systemtyp')" for="systeminstanz-systemtyp">Systemtyp</label>
                        <select class="form-control" id="systeminstanz-systemtyp" name="systemtyp" v-model="$v.systeminstanz.systemtypId.$model" required>
                            <option v-if="!systeminstanz.systemtypId" v-bind:value="null" selected></option>
                            <option v-bind:value="systemtypOption.id" v-for="systemtypOption in systemtyps" :key="systemtypOption.id">{{systemtypOption.bezeichnung}}</option>
                        </select>
                    </div>
                    <div v-if="$v.systeminstanz.systemtypId.$anyDirty && $v.systeminstanz.systemtypId.$invalid">
                        <small class="form-text text-danger" v-if="!$v.systeminstanz.systemtypId.required" v-text="$t('entity.validation.required')">
                            This field is required.
                        </small>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.betriebsstaette')" for="systeminstanz-betriebsstaette">Betriebsstaette</label>
                        <select class="form-control" id="systeminstanz-betriebsstaette" name="betriebsstaette" v-model="systeminstanz.betriebsstaetteId">
                            <option v-bind:value="null"></option>
                            <option v-bind:value="betriebsstaetteOption.id" v-for="betriebsstaetteOption in betriebsstaettes" :key="betriebsstaetteOption.id">
                                [{{betriebsstaetteOption.bsnr}}] {{betriebsstaetteOption.strasse}} {{betriebsstaetteOption.hausnummer}}; {{betriebsstaetteOption.plz}} {{betriebsstaetteOption.ort}}
                            </option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systeminstanz.betreiber')" for="systeminstanz-betreiber">Betreiber</label>
                        <select class="form-control" id="systeminstanz-betreiber" name="betreiber" v-model="systeminstanz.betreiberId">
                            <option v-bind:value="null"></option>
                            <option v-bind:value="betreiberOption.id" v-for="betreiberOption in betreibers" :key="betreiberOption.id">
                                {{betreiberOption.vorname}}, {{betreiberOption.nachname}} -  {{betreiberOption.strasse}} {{betreiberOption.hausnummer}} {{betreiberOption.plz}} {{betreiberOption.ort}}
                            </option>
                        </select>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.systeminstanz.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./systeminstanz-update.component.ts">
</script>

<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.betreiber.home.createOrEditLabel" v-text="$t('gdb3App.betreiber.home.createOrEditLabel')">Create or edit a Betreiber</h2>
                <div>
                    <div class="form-group" v-if="betreiber.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="betreiber.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betreiber.vorname')" for="betreiber-vorname">Vorname</label>
                        <input type="text" class="form-control" name="vorname" id="betreiber-vorname"
                            :class="{'valid': !$v.betreiber.vorname.$invalid, 'invalid': $v.betreiber.vorname.$invalid }" v-model="$v.betreiber.vorname.$model"  required/>
                        <div v-if="$v.betreiber.vorname.$anyDirty && $v.betreiber.vorname.$invalid">
                            <small class="form-text text-danger" v-if="!$v.betreiber.vorname.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betreiber.nachname')" for="betreiber-nachname">Nachname</label>
                        <input type="text" class="form-control" name="nachname" id="betreiber-nachname"
                            :class="{'valid': !$v.betreiber.nachname.$invalid, 'invalid': $v.betreiber.nachname.$invalid }" v-model="$v.betreiber.nachname.$model"  required/>
                        <div v-if="$v.betreiber.nachname.$anyDirty && $v.betreiber.nachname.$invalid">
                            <small class="form-text text-danger" v-if="!$v.betreiber.nachname.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betreiber.strasse')" for="betreiber-strasse">Strasse</label>
                        <input type="text" class="form-control" name="strasse" id="betreiber-strasse"
                            :class="{'valid': !$v.betreiber.strasse.$invalid, 'invalid': $v.betreiber.strasse.$invalid }" v-model="$v.betreiber.strasse.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betreiber.hausnummer')" for="betreiber-hausnummer">Hausnummer</label>
                        <input type="text" class="form-control" name="hausnummer" id="betreiber-hausnummer"
                            :class="{'valid': !$v.betreiber.hausnummer.$invalid, 'invalid': $v.betreiber.hausnummer.$invalid }" v-model="$v.betreiber.hausnummer.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betreiber.plz')" for="betreiber-plz">Plz</label>
                        <input type="text" class="form-control" name="plz" id="betreiber-plz"
                            :class="{'valid': !$v.betreiber.plz.$invalid, 'invalid': $v.betreiber.plz.$invalid }" v-model="$v.betreiber.plz.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betreiber.ort')" for="betreiber-ort">Ort</label>
                        <input type="text" class="form-control" name="ort" id="betreiber-ort"
                            :class="{'valid': !$v.betreiber.ort.$invalid, 'invalid': $v.betreiber.ort.$invalid }" v-model="$v.betreiber.ort.$model" />
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.betreiber.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./betreiber-update.component.ts">
</script>

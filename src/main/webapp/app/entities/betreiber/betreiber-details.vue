<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="betreiber">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.betreiber.detail.title')">Betreiber</span> {{betreiber.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.betreiber.vorname')">Vorname</span>
                    </dt>
                    <dd>
                        <span>{{betreiber.vorname}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betreiber.nachname')">Nachname</span>
                    </dt>
                    <dd>
                        <span>{{betreiber.nachname}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betreiber.strasse')">Strasse</span>
                    </dt>
                    <dd>
                        <span>{{betreiber.strasse}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betreiber.hausnummer')">Hausnummer</span>
                    </dt>
                    <dd>
                        <span>{{betreiber.hausnummer}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betreiber.plz')">Plz</span>
                    </dt>
                    <dd>
                        <span>{{betreiber.plz}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betreiber.ort')">Ort</span>
                    </dt>
                    <dd>
                        <span>{{betreiber.ort}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="betreiber.id" :to="{name: 'BetreiberEdit', params: {betreiberId: betreiber.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./betreiber-details.component.ts">
</script>

<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="arzt">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.arzt.detail.title')">Arzt</span> {{arzt.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.arzt.lanr')">Lanr</span>
                    </dt>
                    <dd>
                        <span>{{arzt.lanr}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.arzt.titel')">Titel</span>
                    </dt>
                    <dd>
                        <span>{{arzt.titel}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.arzt.vorname')">Vorname</span>
                    </dt>
                    <dd>
                        <span>{{arzt.vorname}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.arzt.nachname')">Nachname</span>
                    </dt>
                    <dd>
                        <span>{{arzt.nachname}}</span>
                    </dd>

                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="arzt.id" :to="{name: 'ArztEdit', params: {arztId: arzt.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./arzt-details.component.ts">
</script>

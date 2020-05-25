package nl.avisi.model;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.UserSyncDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorklogMutator {

    /**
     * Creates WorklogDTOs from the passed in JSONArray and puts them in a list
     *
     * @param jsonArray All retrieved worklogs in jsonArray form.
     * @return List of all worklogs that were retrieved between the two given dates for the specified workers.
     */
    public List<OriginWorklogDTO> createWorklogDTOs(JSONArray jsonArray) {
        List<OriginWorklogDTO> worklogs = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            try {
                String worker = jsonObject.getString("worker");
                String started = jsonObject.getString("started");
                String originTaskId = jsonObject.getJSONObject("issue").getString("accountKey");
                int timeSpentSeconds = jsonObject.getInt("timeSpentSeconds");
                int worklogId = jsonObject.getInt("tempoWorklogId");

                worklogs.add(
                        (OriginWorklogDTO) new OriginWorklogDTO()
                                .setWorklogId(worklogId)
                                .setWorker(worker)
                                .setStarted(started)
                                .setOriginTaskId(originTaskId)
                                .setTimeSpentSeconds(timeSpentSeconds)
                );

            } catch (JSONException e) {
                return new ArrayList<>();
            }
        }

        return worklogs;
    }

    /**
     * Filters out worklogs from the worklogs retrieved from the origin server by
     * comparing the already synced worklogIds and removing any match from the original
     * list.
     *
     * @param retrievedWorklogs Worklogs that were retrieved from the origin server
     * @param allWorklogIds All worklogIds of worklogs that are already synced in the past.
     *                      This data is retrieved from the database
     * @return list of DestinationWorklogDTOs that only contain not yet synced worklogs
     */
    public List<DestinationWorklogDTO> filterOutAlreadySyncedWorklogs(List<OriginWorklogDTO> retrievedWorklogs, List<Integer> allWorklogIds) {
        return retrievedWorklogs
                .stream()
                .filter(worklog -> allWorklogIds.stream()
                        .noneMatch(worklogId -> worklogId == worklog.getWorklogId()))
                .collect(Collectors.toList());
    }

    /**
     * Filters all worklogs retrieved from the origin server
     * that match with the worklogs that were posted to the
     * destination server and have a status code 200.
     *
     * @param allRetrievedWorklogsFromOriginServer All the worklogs that were retrieved from the origin server
     * @param postedWorklogsWithResponseCodes Map of worklogs that were posted with the respective response status
     * @return List of all the worklogIds that had a status code of 200
     */
    public List<Integer> filterOutFailedPostedWorklogs(List<OriginWorklogDTO> allRetrievedWorklogsFromOriginServer, Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes) {
        List<Integer> idsOfSuccesfullyPostedworklogs = new ArrayList<>();

        postedWorklogsWithResponseCodes.forEach((key, value) -> allRetrievedWorklogsFromOriginServer.forEach(worklog -> {
            if (worklog.equals(key) && value == 200) {
                idsOfSuccesfullyPostedworklogs.add(worklog.getWorklogId());
            }
        }));

        return idsOfSuccesfullyPostedworklogs;
    }

    /**
     * Maps the worker field of a destinationWorklogDTO after it has been set with
     * the origin worker user key, to the destination worker key that matches the
     * origin worker user key.
     *
     * @param worklogsToBeSynced List of DestinationWorklogDTO where the worker field contains the origin user key
     *                           which will be swapped for the destination user key
     * @param autoSyncUsers List of all the users that have auto sync enabled
     * @return A list of worklogs with the correct user key mapped to the worker field
     */
    public List<DestinationWorklogDTO> replaceOriginUserKeyWithCorrectDestinationUserKey(List<DestinationWorklogDTO> worklogsToBeSynced, List<UserSyncDTO> autoSyncUsers) {

        List<DestinationWorklogDTO> worklogsWithoutMatchingKey = new ArrayList<>();

        worklogsToBeSynced.forEach(worklog -> {
            Optional<String> matchingKey = autoSyncUsers.stream()
                    .filter(user -> user.getOriginWorker().equals(worklog.getWorker()))
                    .map(UserSyncDTO::getDestinationWorker)
                    .reduce((u, v) -> {
                        throw new IllegalStateException("More than one user key found");
                    });

            if (matchingKey.isPresent()) {
                worklog.setWorker(matchingKey.get());
            } else {
                worklogsWithoutMatchingKey.add(worklog);
            }
        });

        worklogsWithoutMatchingKey.forEach(worklogsToBeSynced::remove);

        return worklogsToBeSynced;
    }
}

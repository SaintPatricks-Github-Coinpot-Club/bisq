/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.statistics;

import bisq.core.app.misc.ExecutableForAppWithP2p;

import bisq.common.app.Version;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatisticsMain extends ExecutableForAppWithP2p {
    public static void main(String[] args) {
        new StatisticsMain().execute(args);
    }

    private Statistics statistics;

    public StatisticsMain() {
        super("Bisq Statsnode", "bisq-statistics", "bisq_statistics", Version.VERSION);
    }

    @Override
    protected void doExecute() {
        super.doExecute();

        checkMemory(config, this);
        keepRunning();
    }

    @Override
    protected void applyInjector() {
        super.applyInjector();
        statistics = new Statistics(injector);
    }

    @Override
    protected void startApplication() {
        super.startApplication();
        statistics.startApplication();
    }
}
